package com.example.poraqui.ui.explore

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.poraqui.R

data class FilterChipItem(
    val name: String,
    val iconEmoji: String = ""
) {
    val displayName: String
        get() = if (iconEmoji.isBlank()) name else "$iconEmoji $name"
}

class CategoryFilterAdapter(
    private var filterItems: List<FilterChipItem> = emptyList(),
    private val onChipSelected: (FilterChipItem) -> Unit
) : RecyclerView.Adapter<CategoryFilterAdapter.FilterViewHolder>() {

    private var selectedPosition = 0

    fun updateList(newList: List<FilterChipItem>) {
        filterItems = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category_filter, parent, false)
        return FilterViewHolder(view)
    }

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        holder.bind(filterItems[position], position == selectedPosition)
    }

    override fun getItemCount(): Int = filterItems.size

    inner class FilterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvChip: TextView = itemView.findViewById(R.id.tv_filter_chip)

        fun bind(item: FilterChipItem, isSelected: Boolean) {
            tvChip.text = item.displayName

            if (isSelected) {
                tvChip.background = ContextCompat.getDrawable(itemView.context, R.drawable.bg_chip_selected)
                tvChip.setTextColor(Color.parseColor("#01140E"))
            } else {
                tvChip.background = ContextCompat.getDrawable(itemView.context, R.drawable.bg_chip_unselected)
                tvChip.setTextColor(Color.parseColor("#FFFFFF"))
            }

            itemView.setOnClickListener {
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION && pos != selectedPosition) {
                    val previousPos = selectedPosition
                    selectedPosition = pos
                    notifyItemChanged(previousPos)
                    notifyItemChanged(selectedPosition)
                    onChipSelected(item)
                }
            }
        }
    }
}