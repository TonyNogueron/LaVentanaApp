package mx.tec.laventana.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import mx.tec.laventana.DetailedInfoActivity
import mx.tec.laventana.R
import mx.tec.laventana.model.Location

class LocationCardAdapter(
    private val context: Context,
    private val dataSource: MutableList<Location>,
    private val itemClickCallback: (Int) -> Unit,
) : RecyclerView.Adapter<LocationCardAdapter.LocationCardViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(locationCardItem: Location)
    }


    private var listener: OnItemClickListener? = null

    fun getDataSource(): MutableList<Location> {
        return dataSource
    }

    class LocationCardViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.location_card, parent, false)) {
        private var placeName: TextView? = null
        private var placeDescription: TextView? = null
        private var placeImage: ImageView? = null
        private var moreInfoButton: Button? = null
        private var viewMapButton: Button? = null

        init {
            placeName = itemView.findViewById(R.id.txtCName) as TextView
            placeDescription = itemView.findViewById(R.id.txtCDescription) as TextView
            placeImage = itemView.findViewById(R.id.imgCImagen) as ImageView
            moreInfoButton = itemView.findViewById(R.id.btnCInfo) as Button
            viewMapButton = itemView.findViewById(R.id.btnCMap) as Button
        }

        fun bindData(location: Location) {
            placeName!!.text = location.name
            placeDescription!!.text = location.description
            Glide.with(itemView.context).load(location.imageURL).into(placeImage!!)

            moreInfoButton!!.setOnClickListener {
                val intent = Intent(itemView.context, DetailedInfoActivity::class.java)
                intent.putExtra("currentLocationName", location.name)
                intent.putExtra("currentLocationDescription", location.description)
                intent.putExtra("currentLocationImageURL", location.imageURL)
                intent.putExtra("currentLocationLatitude", location.latitude)
                intent.putExtra("currentLocationLongitude", location.longitude)
                intent.putExtra("currentLocationCategory1", "Category 1")
                intent.putExtra("currentLocationCategory2", "Category 2")
                startActivity(itemView.context, intent, null)
            }

            viewMapButton!!.setOnClickListener {
                val destinationLat = location.latitude
                val destinationLng = location.longitude

                val uri =
                    "https://www.google.com/maps/dir/?api=1&destination=$destinationLat,$destinationLng"

                val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                mapIntent.setPackage("com.google.android.apps.maps")

                if (mapIntent.resolveActivity(it.context.packageManager) != null) {
                    startActivity(it.context, mapIntent, null)
                } else {
                    val webMapIntent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                    startActivity(it.context, webMapIntent, null)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationCardViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return LocationCardViewHolder(inflater, parent)
    }

    override fun getItemCount(): Int {
        return dataSource.size
    }

    override fun onBindViewHolder(holder: LocationCardViewHolder, position: Int) {
        val location = dataSource[position]
        holder.bindData(location)

        holder.itemView.setOnClickListener {
            listener?.onItemClick(location)
            itemClickCallback(position)
        }
    }

    fun updateData(locations: MutableList<Location>) {
        dataSource.clear()
        dataSource.addAll(locations)
        notifyDataSetChanged()
    }


}