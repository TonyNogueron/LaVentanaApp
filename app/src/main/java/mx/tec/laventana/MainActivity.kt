package mx.tec.laventana

import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mx.tec.laventana.databinding.ActivityMainBinding
import mx.tec.laventana.model.Location
import mx.tec.laventana.utility.AppDatabase

class MainActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding

    private var dataFetched = false

//    private val cien_natural_location = LatLng(21.02399893023223, -89.62432365666739)
//    private val universidad_del_caribe = LatLng(21.20043752948293, -86.82345426948685)
//
//    private val mestiza = LatLng(21.00937863348511, -88.30693033017081)
//
//    private val soumayaMuseumLocation = LatLng(19.440057, -99.204380)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)
        binding.bottomNavigationView.selectedItemId = R.id.mapsFragment


//        val db = AppDatabase.getInstance(this)
//
//        val placesNames: MutableList<String> =
//            mutableListOf(
//                "Soumaya Museum",
//                "100% Natural Mérida",
//                "Universidad del Caribe",
//                "Mestiza de Indias"
//            )
//        val placesDescriptions: MutableList<String> =
//            mutableListOf(
//                "Soumaya Museum",
//                "100% Natural Mérida",
//                "Universidad del Caribe",
//                "Mestiza de Indias"
//            )
//        val placesImagesURL: MutableList<String> = mutableListOf(
//            "https://cdn-3.expansion.mx/dims4/default/904e495/2147483647/strip/true/crop/1274x728+0+0/resize/1200x686!/format/webp/quality/60/?url=https%3A%2F%2Fcdn-3.expansion.mx%2F26%2F6a%2F198af16d4e579ec56f9aa910c776%2Fcaptura5.JPG",
//            "https://www.100natural.com/images/sucursales/cafe-del-muelle.jpg",
//            "https://l21.mx/wp-content/uploads/2017/08/ucaribe1.jpg",
//            "https://images.squarespace-cdn.com/content/v1/53444301e4b0323896c3a9d7/1523655311894-9LLZ8282IMEDFYRTR1MK/mestiza1?format=2500w"
//        )
//        val placesLatitude: MutableList<Double> = mutableListOf(
//            soumayaMuseumLocation.latitude,
//            cien_natural_location.latitude,
//            universidad_del_caribe.latitude,
//            mestiza.latitude
//        )
//        val placesLongitude: MutableList<Double> = mutableListOf(
//            soumayaMuseumLocation.longitude,
//            cien_natural_location.longitude,
//            universidad_del_caribe.longitude,
//            mestiza.longitude
//        )
//
//        Thread {
//            if (db.locationDao().getLocations().isEmpty()) {
//                placesNames.forEachIndexed { index, placeName ->
//                    val newLocation = Location(
//                        idLocation = 0,
//                        name = placeName,
//                        description = placesDescriptions[index],
//                        imageURL = placesImagesURL[index],
//                        latitude = placesLatitude[index],
//                        longitude = placesLongitude[index],
//                        category = "Category 1"
//                    )
//                    db.locationDao().registerLocation(newLocation)
//                }
//            }
//        }.start()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
