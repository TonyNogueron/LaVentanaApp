package mx.tec.laventana

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide

class DetailedInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed_info)

        val imgPlace = findViewById<ImageView>(R.id.imgPlace)
        val txtAddress = findViewById<TextView>(R.id.txtAddress)
        val txtCat1 = findViewById<TextView>(R.id.txtCat1)
        val txtDescription = findViewById<TextView>(R.id.txtDescription)
        val getDirectionsButton = findViewById<TextView>(R.id.btnVerMapa)


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

        getDirectionsButton.setOnClickListener {
            val uri =
                "https://www.google.com/maps/dir/?api=1&destination=$locationLatitude,$locationLongitude"

            val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            mapIntent.setPackage("com.google.android.apps.maps")

            if (mapIntent.resolveActivity(it.context.packageManager) != null) {
                ContextCompat.startActivity(it.context, mapIntent, null)
            } else {
                val webMapIntent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                ContextCompat.startActivity(it.context, webMapIntent, null)
            }
        }

        txtAddress.text = locationName
        txtDescription.text = locationDescription
        txtCat1.text = locationCategory1
        Glide.with(this).load(locationImageURL).into(imgPlace)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true
    }

}
