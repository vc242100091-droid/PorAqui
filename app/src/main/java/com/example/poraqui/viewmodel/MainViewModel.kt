package com.example.poraqui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.poraqui.data.model.Business
import com.example.poraqui.data.model.Category
import com.example.poraqui.data.repository.BusinessRepository
import com.google.firebase.auth.FirebaseAuth

class MainViewModel(
    private val repository: BusinessRepository = BusinessRepository(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) : ViewModel() {

    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> get() = _userName

    private val _userInitial = MutableLiveData<String>()
    val userInitial: LiveData<String> get() = _userInitial

    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>> get() = _categories

    private val _businesses = MutableLiveData<List<Business>>()
    val businesses: LiveData<List<Business>> get() = _businesses

    private val allBusinesses = mutableListOf<Business>()

    init {
        loadUserData()
        loadHomeData()
    }

    private fun loadUserData() {
        val currentUser = auth.currentUser
        val name = when {
            currentUser?.displayName?.isNotBlank() == true -> currentUser.displayName!!
            currentUser?.email?.isNotBlank() == true -> currentUser.email!!.substringBefore("@").replaceFirstChar { it.uppercase() }
            else -> "Sofía"
        }
        _userName.value = name
        _userInitial.value = name.take(1).uppercase()
    }

    private fun loadHomeData() {
        _categories.value = repository.getCategories()
        val list = repository.getNearbyBusinesses()
        allBusinesses.clear()
        allBusinesses.addAll(list)
        _businesses.value = list
    }

    fun filterBusinesses(query: String) {
        val trimmed = query.trim()
        if (trimmed.isEmpty()) {
            _businesses.value = allBusinesses
        } else {
            _businesses.value = allBusinesses.filter {
                it.name.contains(trimmed, ignoreCase = true) ||
                        it.category.contains(trimmed, ignoreCase = true) ||
                        it.location.contains(trimmed, ignoreCase = true)
            }
        }
    }
}