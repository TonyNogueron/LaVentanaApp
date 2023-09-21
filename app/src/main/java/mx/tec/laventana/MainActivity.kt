package mx.tec.laventana

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import mx.tec.laventana.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(MapsFragment())
        binding.bottomNavigationView.selectedItemId = R.id.homeMenuItem

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homeMenuItem -> {
                    replaceFragment(MapsFragment())
                    true
                }

                R.id.searchMenuItem -> {
                    replaceFragment(Search())
                    true
                }

                R.id.placesListMenuItem -> {
                    replaceFragment(PlacesList())
                    true
                }

                else -> false
            }

        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()

        val existingFragment = fragmentManager.findFragmentByTag(fragment.javaClass.simpleName)

        if (existingFragment == null) {
            fragmentTransaction.add(
                R.id.fragment_container, fragment, fragment.javaClass.simpleName
            )
        } else {
            fragmentTransaction.show(existingFragment)
        }

        for (existingFrag in fragmentManager.fragments) {
            if (existingFrag != existingFragment) {
                fragmentTransaction.hide(existingFrag)
            }
        }

        fragmentTransaction.commit()
    }

}