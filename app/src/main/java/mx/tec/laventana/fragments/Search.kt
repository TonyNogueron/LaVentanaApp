package mx.tec.laventana.fragments

import android.annotation.SuppressLint
import android.content.Context
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
import mx.tec.laventana.R
import mx.tec.laventana.model.Location
import mx.tec.laventana.utility.AppDatabase

class Search : Fragment() {

    private var places: MutableList<Location> = mutableListOf()
    private lateinit var result1: TextView
    private lateinit var result2: TextView
    private lateinit var result3: TextView

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_search, container, false)

        val editText = rootView.findViewById<EditText>(R.id.editTextText)
        result1 = rootView.findViewById(R.id.textViewResult1)
        result2 = rootView.findViewById(R.id.textViewResult2)
        result3 = rootView.findViewById(R.id.textViewResult3)

        editText.requestFocus()

        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)

        editText.setOnTouchListener { v, event ->
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
            filterSuggestions("")
        }.start()

        editText.addTextChangedListener {
            val searchText = it.toString()
            filterSuggestions(searchText)
        }

        return rootView
    }

    private fun filterSuggestions(query: String) {
        clearSuggestions()

        var suggestionCount = 0
        if (places.isNotEmpty()) {
            for (place in places) {
                if (place.name.contains(query, ignoreCase = true)) {
                    when (suggestionCount) {
                        0 -> result1.text = place.name
                        1 -> result2.text = place.name
                        2 -> result3.text = place.name
                    }
                    suggestionCount++
                    if (suggestionCount >= 3) {
                        break
                    }
                }
            }
        }
    }

    private fun clearSuggestions() {
        result1.text = ""
        result2.text = ""
        result3.text = ""
    }
}
