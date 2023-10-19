package mx.tec.laventana

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import mx.tec.laventana.databinding.ActivityMainBinding
import mx.tec.laventana.utility.App
import mx.tec.laventana.utility.DataFetcher

class MainActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding

    private var dataFetched = false

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

        DataFetcher(this).fetchDataFromAPI()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
