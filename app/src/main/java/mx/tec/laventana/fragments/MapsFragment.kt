package mx.tec.laventana.fragments;

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import mx.tec.laventana.R

class MapsFragment : Fragment() {

    private lateinit var map: GoogleMap


    private val cien_natural_location = LatLng(21.02399893023223, -89.62432365666739)
    private val universidad_del_caribe = LatLng(21.20043752948293, -86.82345426948685)

    private val soumayaMuseumLocation = LatLng(19.440057, -99.204380)
    private var currentCameraPosition = cien_natural_location
    private var currentZoom = 8f


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync { googleMap ->
            map = googleMap
            map.addMarker(
                MarkerOptions().position(soumayaMuseumLocation).title("Soumaya Museum")
            )
            map.addMarker(
                MarkerOptions().position(cien_natural_location).title("100% Natural Mérida")
            )
            map.addMarker(
                MarkerOptions().position(universidad_del_caribe).title("Universidad del Caribe")
            )

            map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentCameraPosition, currentZoom))
        }
    }

    override fun onPause() {
        super.onPause()
        currentCameraPosition = map.cameraPosition.target
        currentZoom = map.cameraPosition.zoom
    }
}
