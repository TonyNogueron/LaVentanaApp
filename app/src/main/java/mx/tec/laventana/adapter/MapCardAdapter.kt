package mx.tec.laventana.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import mx.tec.laventana.R
import mx.tec.laventana.model.Location

class MapCardAdapter(
    private val context: Context,
    private val dataSource: MutableList<Location>,
    private val itemClickCallback: (Int) -> Unit
) : RecyclerView.Adapter<MapCardAdapter.MapCardViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(mapCardItem: Location)
    }

    private var listener: OnItemClickListener? = null

    fun getDataSource(): MutableList<Location> {
        return dataSource
    }

    class MapCardViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.map_card, parent, false)) {
        private var placeName: TextView? = null
        private var placeDescription: TextView? = null
        private var placeImage: ImageView? = null

        init {
            placeName = itemView.findViewById(R.id.mapCardTitle) as TextView
            placeDescription = itemView.findViewById(R.id.mapCardDescription) as TextView
            placeImage = itemView.findViewById(R.id.mapCardImageView) as ImageView
        }

        fun bindData(location: Location) {
            placeName!!.text = location.name
            placeDescription!!.text = location.description
            Glide.with(itemView.context).load(location.imageURL).into(placeImage!!)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MapCardViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MapCardViewHolder(inflater, parent)
    }

    override fun getItemCount(): Int {
        return dataSource.size
    }

    override fun onBindViewHolder(holder: MapCardViewHolder, position: Int) {
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

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
}
