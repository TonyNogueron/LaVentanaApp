package mx.tec.laventana.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import mx.tec.laventana.DetailedInfoActivity
import mx.tec.laventana.R
import mx.tec.laventana.model.Location
import mx.tec.laventana.utility.AppDatabase

class Search : Fragment() {

    private var places: MutableList<Location> = mutableListOf()
    private lateinit var result1: TextView
    private lateinit var result2: TextView
    private lateinit var result3: TextView

    private lateinit var result1Container: View
    private lateinit var result2Container: View
    private lateinit var result3Container: View

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_search, container, false)

        val editText = rootView.findViewById<EditText>(R.id.editTextText)
        result1 = rootView.findViewById(R.id.textViewResult1)
        result2 = rootView.findViewById(R.id.textViewResult2)
        result3 = rootView.findViewById(R.id.textViewResult3)

        result1Container = rootView.findViewById(R.id.result1Container)
        result2Container = rootView.findViewById(R.id.result2Container)
        result3Container = rootView.findViewById(R.id.result3Container)

        editText.requestFocus()

        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)

        editText.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val editTextRight = editText.right

                val clearIconRight =
                    editTextRight - editText.compoundDrawables[2].bounds.width() * 2

                if (event.rawX >= clearIconRight && event.rawX <= editTextRight) {
                    editText.text.clear()
                    return@setOnTouchListener true
                }
            }
            false
        }

        val db = AppDatabase.getInstance(requireContext())

        Thread {
            places = db.locationDao().getLocations()
        }.start()

        editText.addTextChangedListener {
            val searchText = it.toString()
            filterSuggestions(searchText)
        }

        result1.setOnClickListener {
            if (result1.text.isNotEmpty()) {
                val location = findLocation(result1.text.toString())
                if (location != null) {
                    onLocationClick(location)
                }
            }
        }

        result2.setOnClickListener {
            if (result2.text.isNotEmpty()) {
                val location = findLocation(result2.text.toString())
                if (location != null) {
                    onLocationClick(location)
                }
            }
        }

        result3.setOnClickListener {
            if (result3.text.isNotEmpty()) {
                val location = findLocation(result3.text.toString())
                if (location != null) {
                    onLocationClick(location)
                }
            }
        }

        return rootView
    }

    private fun filterSuggestions(query: String) {
        var suggestionCount = 0
        if (places.isNotEmpty()) {
            for (place in places) {
                if (place.name.contains(query, ignoreCase = true)) {
                    when (suggestionCount) {
                        0 -> {
                            result1.text = place.name
                            result1Container.visibility = View.VISIBLE
                        }

                        1 -> {
                            result2.text = place.name
                            result2Container.visibility = View.VISIBLE
                        }

                        2 -> {
                            result3.text = place.name
                            result3Container.visibility = View.VISIBLE
                        }
                    }
                    suggestionCount++
                    if (suggestionCount >= 3) {
                        break
                    }
                }
            }
        }

        // Set visibility for containers with no results
        if (suggestionCount == 0) {
            result1Container.visibility = View.GONE
            result2Container.visibility = View.GONE
            result3Container.visibility = View.GONE
        } else if (suggestionCount == 1) {
            result2Container.visibility = View.GONE
            result3Container.visibility = View.GONE
        } else if (suggestionCount == 2) {
            result3Container.visibility = View.GONE
        }
    }

    private fun findLocation(locationName: String): Location? {
        return places.find { it.name == locationName }
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
}
