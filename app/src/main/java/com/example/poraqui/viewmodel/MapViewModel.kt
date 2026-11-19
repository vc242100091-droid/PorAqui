package com.example.poraqui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.poraqui.data.model.Business
import com.example.poraqui.data.repository.BusinessRepository

class MapViewModel(
    private val repository: BusinessRepository = BusinessRepository()
) : ViewModel() {

    private val _businesses = MutableLiveData<List<Business>>()
    val businesses: LiveData<List<Business>> get() = _businesses

    private val _selectedBusiness = MutableLiveData<Business>()
    val selectedBusiness: LiveData<Business> get() = _selectedBusiness

    private val _mapHeaderTitle = MutableLiveData<String>("Negocios cerca de Sonsonate")
    val mapHeaderTitle: LiveData<String> get() = _mapHeaderTitle

    private val _radiusText = MutableLiveData<String>("Radio actual: 3.5 km")
    val radiusText: LiveData<String> get() = _radiusText

    private val allBusinesses = mutableListOf<Business>()

    init {
        loadData()
    }

    private fun loadData() {
        repository.listenBusinessesFromFirestore { list ->
            allBusinesses.clear()
            allBusinesses.addAll(list)
            _businesses.value = list

            if (list.isNotEmpty() && _selectedBusiness.value == null) {
                _selectedBusiness.value = list.first()
            }
        }
    }

    fun selectBusiness(business: Business) {
        _selectedBusiness.value = business
    }

    fun selectBusinessById(id: String) {
        val found = allBusinesses.find { it.id == id }
        if (found != null) {
            _selectedBusiness.value = found
        }
    }

    fun selectBusinessByName(name: String) {
        val found = allBusinesses.find { it.name.contains(name, ignoreCase = true) }
        if (found != null) {
            _selectedBusiness.value = found
        }
    }
}