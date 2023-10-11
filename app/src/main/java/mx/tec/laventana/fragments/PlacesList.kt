package mx.tec.laventana.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import mx.tec.laventana.R
import mx.tec.laventana.adapter.LocationCardAdapter
import mx.tec.laventana.model.Location
import mx.tec.laventana.utility.AppDatabase

class PlacesList : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var placesAdapter: LocationCardAdapter
    private lateinit var places: MutableList<Location>
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_places_list, container, false)
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById<RecyclerView>(R.id.rvPlacesList)
        places = mutableListOf()

        sharedPreferences =
            requireActivity().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)

        val db = AppDatabase.getInstance(requireContext())

        GlobalScope.launch(Dispatchers.IO) {
            places = db.locationDao().getLocations()
            withContext(Dispatchers.Main) {
                placesAdapter = LocationCardAdapter(requireContext(), places) { position ->
                    onLocationClick(places[position])
                }
                placesAdapter.setOnItemClickListener(object :
                    LocationCardAdapter.OnItemClickListener {
                    override fun onItemClick(locationCardItem: Location) {
                        onLocationClick(locationCardItem)
                    }
                }
                )
                recyclerView.apply {
                    adapter = placesAdapter
                    setHasFixedSize(true)
                    layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
                        requireContext(),
                        androidx.recyclerview.widget.LinearLayoutManager.VERTICAL,
                        false
                    )
                }
            }
        }
    }

    // TODO: FIX NAVIGATION
    private fun onLocationClick(location: Location) {
        saveCurrentLocation(location)

        val navController = NavHostFragment.findNavController(this)
        navController.navigate(R.id.action_placesList_to_moreInfoFragment)
    }

    private fun saveCurrentLocation(location: Location) {
        val editor = sharedPreferences.edit()
        editor.putString("currentLocationName", location.name)
        editor.putString("currentLocationDescription", location.description)
        editor.putString("currentLocationImageURL", location.imageURL)
        editor.putFloat("currentLocationLatitude", location.latitude.toFloat())
        editor.putFloat("currentLocationLongitude", location.longitude.toFloat())
        editor.apply()
    }
}
