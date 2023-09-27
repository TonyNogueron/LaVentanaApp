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
import androidx.fragment.app.Fragment
import mx.tec.laventana.R

class Search : Fragment() {
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_search, container, false)

        val editText = rootView.findViewById<EditText>(R.id.editTextText)
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
                    // Handle clear icon click here
                    editText.text.clear()
                    return@setOnTouchListener true
                }
            }
            false
        }

        return rootView
    }
}
