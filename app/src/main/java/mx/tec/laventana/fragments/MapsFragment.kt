package mx.tec.laventana.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import mx.tec.laventana.DetailedInfoActivity
import mx.tec.laventana.R
import mx.tec.laventana.adapter.MapCardAdapter
import mx.tec.laventana.model.Location
import mx.tec.laventana.utility.AppDatabase

class MapsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var viewPager: ViewPager2
    private lateinit var mapCardAdapter: MapCardAdapter
    private lateinit var allPlacesButton: FloatingActionButton
    private lateinit var mapFragment: SupportMapFragment

    private var selectedViewPagerItem = -1
    private lateinit var places: MutableList<Location>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        allPlacesButton = view.findViewById(R.id.showAllButton)

        allPlacesButton.setOnClickListener {
            if (places.isNotEmpty()) {
                val builder = LatLngBounds.builder()
                addMarkers(places, builder)
                map.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 50))
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.clear()
        map.uiSettings.isZoomControlsEnabled = false

        loadPlacesAndSetBounds()
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun loadPlacesAndSetBounds() {
        val builder = LatLngBounds.builder()

        val db = AppDatabase.getInstance(requireContext())

        GlobalScope.launch(Dispatchers.IO) {
            places = db.locationDao().getLocations()
            withContext(Dispatchers.Main) {
                if (places.isNotEmpty()) {
                    addMarkers(places, builder)
                    setMapClickListener(places)

                    viewPager = view?.findViewById(R.id.viewPager) ?: return@withContext
                    setupViewPager(places)
                    updateSelectedViewPagerItem()

                    GlobalScope.launch(Dispatchers.IO) {
                        withContext(Dispatchers.Main) {
                            mapCardAdapter.updateData(places)
                        }
                    }

                    if (isLocationPermissionGranted()) {
                        showUserLocation()
                    } else {
                        requestLocationPermissions()
                    }

                    moveCameraToBound(builder)
                }
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun addMarkers(places: List<Location>, builder: LatLngBounds.Builder) {
        for (place in places) {
            val location = LatLng(place.latitude, place.longitude)
            GlobalScope.launch(Dispatchers.Main) {
                map.addMarker(
                    MarkerOptions().position(location).title(place.name)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marcador48px))
                )
            }
            builder.include(location)
        }
    }

    private fun setMapClickListener(places: List<Location>) {
        map.setOnMarkerClickListener { marker ->
            val position = places.indexOfFirst { it.name == marker.title }
            if (position != -1) {
                selectedViewPagerItem = position
                viewPager.currentItem = position
            }
            true
        }
    }

    private fun onLocationClick(location: Location) {
        val intent = Intent(requireContext(), DetailedInfoActivity::class.java).apply {
            putExtra("currentLocationName", location.name)
            putExtra("currentLocationDescription", location.description)
            putExtra("currentLocationImageURL", location.imageURL)
            putExtra("currentLocationLatitude", location.latitude.toFloat())
            putExtra("currentLocationLongitude", location.longitude.toFloat())
            putExtra("currentLocationCategory1", location.category)
        }
        startActivity(intent)
    }

    private fun moveCameraToBound(builder: LatLngBounds.Builder) {
        val bounds = builder.build()
        val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 50)
        map.moveCamera(cameraUpdate)
    }

    private fun setupViewPager(places: List<Location>) {
        mapCardAdapter = MapCardAdapter(requireContext(), mutableListOf()) { position ->
            selectedViewPagerItem = position
            viewPager.currentItem = position
            updateCameraPosition(position, places)
        }

        mapCardAdapter.setOnItemClickListener(object : MapCardAdapter.OnItemClickListener {
            override fun onItemClick(mapCardItem: Location) {
                onLocationClick(mapCardItem)
            }
        })

        if (selectedViewPagerItem != -1) {
            viewPager.setCurrentItem(selectedViewPagerItem, false)
        } else {
            selectedViewPagerItem = 0
            viewPager.setCurrentItem(0, false)
        }

        viewPager.adapter = mapCardAdapter
        viewPager.clipToPadding = false
        viewPager.clipChildren = false
        viewPager.offscreenPageLimit = 3
        viewPager.getChildAt(0).overScrollMode = ViewPager2.OVER_SCROLL_NEVER
    }

    private fun updateSelectedViewPagerItem() {
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateCameraPosition(position, mapCardAdapter.getDataSource())
            }
        })
    }

    private fun updateCameraPosition(position: Int, places: List<Location>) {
        if (position >= 0 && position < places.size) {
            val location = places[position]
            val markerLocation = LatLng(location.latitude, location.longitude)
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(markerLocation, 13.5f))
        }
    }

    private fun isLocationPermissionGranted(): Boolean {
        return (ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestLocationPermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    @SuppressLint("MissingPermission")
    private fun showUserLocation() {
        if (isLocationPermissionGranted()) {
            val fusedLocationClient =
                LocationServices.getFusedLocationProviderClient(requireContext())
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: android.location.Location? ->
                    if (location != null) {
                        map.isMyLocationEnabled = true
                    }
                }
        } else {
            requestLocationPermissions()
        }
    }


    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }
}
