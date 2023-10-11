package mx.tec.laventana.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import mx.tec.laventana.R

class MoreInfoFragment : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_more_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences =
            requireActivity().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)

        val imgPlace = view.findViewById<ImageView>(R.id.imgPlace)
        val txtAddress = view.findViewById<TextView>(R.id.txtAddress)
        val txtCat1 = view.findViewById<TextView>(R.id.txtCat1)
        val txtCat2 = view.findViewById<TextView>(R.id.txtCat2)
        val txtCat3 = view.findViewById<TextView>(R.id.txtCat3)
        val txtCat4 = view.findViewById<TextView>(R.id.txtCat4)
        val txtDescription = view.findViewById<TextView>(R.id.txtDescription)

        val locationName = sharedPreferences.getString("currentLocationName", "")
        val locationDescription = sharedPreferences.getString("currentLocationDescription", "")
        val locationImageURL = sharedPreferences.getString("currentLocationImageURL", "")
        val locationLatitude = sharedPreferences.getFloat("currentLocationLatitude", 0.0f)
        val locationLongitude = sharedPreferences.getFloat("currentLocationLongitude", 0.0f)

        txtAddress.text = locationName
        txtCat1.text = "Category 1"
        txtCat2.text = "Category 2"
        txtCat3.text = "Category 3"
        txtCat4.text = "Category 4"
        txtDescription.text = locationDescription
        Glide.with(requireContext()).load(locationImageURL).into(imgPlace)
    }

}
