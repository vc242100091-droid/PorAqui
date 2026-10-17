package com.example.poraqui.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.poraqui.R
import com.example.poraqui.data.model.Category

class CategoryAdapter(
    private var categories: List<Category> = emptyList(),
    private val onItemClick: (Category) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    fun updateList(newList: List<Category>) {
        categories = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(categories[position])
    }

    override fun getItemCount(): Int = categories.size

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvIcon: TextView = itemView.findViewById(R.id.tv_category_icon)
        private val tvName: TextView = itemView.findViewById(R.id.tv_category_name)

        fun bind(category: Category) {
            tvIcon.text = category.iconEmoji
            tvName.text = category.name
            itemView.setOnClickListener { onItemClick(category) }
        }
    }
}