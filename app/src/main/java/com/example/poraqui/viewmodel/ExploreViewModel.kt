package com.example.poraqui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.poraqui.data.model.Business
import com.example.poraqui.data.repository.BusinessRepository
import com.example.poraqui.ui.explore.FilterChipItem

class ExploreViewModel(
    private val repository: BusinessRepository = BusinessRepository()
) : ViewModel() {

    private val _filterChips = MutableLiveData<List<FilterChipItem>>()
    val filterChips: LiveData<List<FilterChipItem>> get() = _filterChips

    private val _businesses = MutableLiveData<List<Business>>()
    val businesses: LiveData<List<Business>> get() = _businesses

    private val _resultsCountText = MutableLiveData<String>()
    val resultsCountText: LiveData<String> get() = _resultsCountText

    private val allBusinesses = mutableListOf<Business>()
    private var currentCategory = "Todos"
    private var currentQuery = ""

    init {
        loadData()
    }

    private fun loadData() {
        val categories = repository.getCategories()
        val chipList = mutableListOf(FilterChipItem("Todos", ""))
        chipList.addAll(categories.map { FilterChipItem(it.name, it.iconEmoji) })
        _filterChips.value = chipList

        val list = repository.getNearbyBusinesses()
        allBusinesses.clear()
        allBusinesses.addAll(list)

        applyFilters()
    }

    fun setSelectedCategory(categoryName: String) {
        currentCategory = categoryName
        applyFilters()
    }

    fun setSearchQuery(query: String) {
        currentQuery = query.trim()
        applyFilters()
    }

    private fun applyFilters() {
        var result = allBusinesses.toList()

        if (currentCategory != "Todos") {
            result = result.filter { it.category.equals(currentCategory, ignoreCase = true) }
        }

        if (currentQuery.isNotEmpty()) {
            result = result.filter {
                it.name.contains(currentQuery, ignoreCase = true) ||
                        it.category.contains(currentQuery, ignoreCase = true) ||
                        it.location.contains(currentQuery, ignoreCase = true)
            }
        }

        _businesses.value = result
        _resultsCountText.value = "${result.size} negocios encontrados"
    }
}