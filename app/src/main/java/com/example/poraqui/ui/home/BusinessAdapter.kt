package com.example.poraqui.ui.home

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.poraqui.R
import com.example.poraqui.data.model.Business

class BusinessAdapter(
    private var businesses: List<Business> = emptyList(),
    private val onItemClick: (Business) -> Unit
) : RecyclerView.Adapter<BusinessAdapter.BusinessViewHolder>() {

    fun updateList(newList: List<Business>) {
        businesses = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusinessViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_business, parent, false)
        return BusinessViewHolder(view)
    }

    override fun onBindViewHolder(holder: BusinessViewHolder, position: Int) {
        holder.bind(businesses[position])
    }

    override fun getItemCount(): Int = businesses.size

    inner class BusinessViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val flIconBg: FrameLayout = itemView.findViewById(R.id.fl_icon_container)
        private val tvIcon: TextView = itemView.findViewById(R.id.tv_business_icon)
        private val tvName: TextView = itemView.findViewById(R.id.tv_business_name)
        private val tvRatingCategory: TextView = itemView.findViewById(R.id.tv_business_rating_category)
        private val tvLocation: TextView = itemView.findViewById(R.id.tv_business_location)
        private val tvStatus: TextView = itemView.findViewById(R.id.tv_business_status)

        fun bind(business: Business) {
            tvIcon.text = business.iconEmoji
            tvName.text = business.name
            tvRatingCategory.text = "★ ${business.rating} · ${business.category}"
            tvLocation.text = "📍 ${business.distance} · ${business.location}"

            if (business.isOpen) {
                tvStatus.text = "• Abierto"
                tvStatus.setTextColor(Color.parseColor("#00FF87"))
            } else {
                tvStatus.text = "• Cerrado"
                tvStatus.setTextColor(Color.parseColor("#F59E0B"))
            }

            try {
                val drawable = GradientDrawable().apply {
                    shape = GradientDrawable.RECTANGLE
                    cornerRadius = 28f
                    setColor(Color.parseColor(business.iconBgColorHex))
                }
                flIconBg.background = drawable
            } catch (e: Exception) {
                // Fallback to default
            }

            itemView.setOnClickListener { onItemClick(business) }
        }
    }
}