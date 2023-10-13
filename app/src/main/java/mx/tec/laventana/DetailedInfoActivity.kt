package mx.tec.laventana

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide

class DetailedInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed_info)

        val imgPlace = findViewById<ImageView>(R.id.imgPlace)
        val txtAddress = findViewById<TextView>(R.id.txtAddress)
        val txtCat1 = findViewById<TextView>(R.id.txtCat1)
        val txtCat2 = findViewById<TextView>(R.id.txtCat2)
        val txtCat3 = findViewById<TextView>(R.id.txtCat3)
        val txtCat4 = findViewById<TextView>(R.id.txtCat4)
        val txtDescription = findViewById<TextView>(R.id.txtDescription)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val locationName = intent.getStringExtra("currentLocationName")
        supportActionBar?.title = locationName

        val locationDescription = intent.getStringExtra("currentLocationDescription")
        val locationImageURL = intent.getStringExtra("currentLocationImageURL")
        val locationLatitude = intent.getFloatExtra("currentLocationLatitude", 0.0f)
        val locationLongitude = intent.getFloatExtra("currentLocationLongitude", 0.0f)

        val locationCategory1 = intent.getStringExtra("currentLocationCategory1")
        if (locationCategory1 != null) {
            txtCat1.text = locationCategory1
        } else {
            txtCat1.visibility = View.GONE
        }

        val locationCategory2 = intent.getStringExtra("currentLocationCategory2")
        if (locationCategory2 != null) {
            txtCat2.text = locationCategory2
        } else {
            txtCat2.visibility = View.GONE
        }

        val locationCategory3 = intent.getStringExtra("currentLocationCategory3")
        if (locationCategory3 != null) {
            txtCat3.text = locationCategory3
        } else {
            txtCat3.visibility = View.GONE
        }

        val locationCategory4 = intent.getStringExtra("currentLocationCategory4")
        if (locationCategory4 != null) {
            txtCat4.text = locationCategory4
        } else {
            txtCat4.visibility = View.GONE
        }

        txtAddress.text = locationName
        txtDescription.text = locationDescription
        Glide.with(this).load(locationImageURL).into(imgPlace)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true
    }

}
