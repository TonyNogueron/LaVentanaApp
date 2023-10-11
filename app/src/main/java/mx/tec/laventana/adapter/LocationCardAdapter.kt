package mx.tec.laventana.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import mx.tec.laventana.R
import mx.tec.laventana.model.Location

class LocationCardAdapter(
    private val context: Context,
    private val dataSource: MutableList<Location>,
    private val itemClickCallback: (Int) -> Unit
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

        val animation = AnimationUtils.loadAnimation(context, R.anim.bounce)
        holder.itemView.startAnimation(animation)
    }

    fun updateData(locations: MutableList<Location>) {
        dataSource.clear()
        dataSource.addAll(locations)
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

}