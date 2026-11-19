package com.example.poraqui.ui.map

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.poraqui.R
import com.example.poraqui.data.model.Business

class MapPinAdapter(
    private var businesses: List<Business> = emptyList(),
    private val onPinClick: (Business) -> Unit
) : RecyclerView.Adapter<MapPinAdapter.PinViewHolder>() {

    fun updateList(newList: List<Business>) {
        businesses = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PinViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_map_pin, parent, false)
        return PinViewHolder(view)
    }

    override fun onBindViewHolder(holder: PinViewHolder, position: Int) {
        holder.bind(businesses[position])
    }

    override fun getItemCount(): Int = businesses.size

    inner class PinViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvLabel: TextView = itemView.findViewById(R.id.tv_pin_label)
        private val tvEmoji: TextView = itemView.findViewById(R.id.tv_pin_emoji)

        fun bind(business: Business) {
            tvLabel.text = "• ${business.name}"
            tvEmoji.text = business.iconEmoji
            itemView.setOnClickListener {
                onPinClick(business)
            }
        }
    }
}