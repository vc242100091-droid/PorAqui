package com.example.poraqui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class ProfileViewModel(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) : ViewModel() {

    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> get() = _userName

    private val _userEmail = MutableLiveData<String>()
    val userEmail: LiveData<String> get() = _userEmail

    private val _userInitial = MutableLiveData<String>()
    val userInitial: LiveData<String> get() = _userInitial

    private val _savedCount = MutableLiveData<String>("12")
    val savedCount: LiveData<String> get() = _savedCount

    private val _reviewsCount = MutableLiveData<String>("4")
    val reviewsCount: LiveData<String> get() = _reviewsCount

    private val _visitsCount = MutableLiveData<String>("8")
    val visitsCount: LiveData<String> get() = _visitsCount

    init {
        loadUserData()
    }

    private fun loadUserData() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val name = when {
                currentUser.displayName?.isNotBlank() == true -> currentUser.displayName!!
                currentUser.email?.isNotBlank() == true -> currentUser.email!!.substringBefore("@").replaceFirstChar { it.uppercase() }
                else -> "Sofía Martínez"
            }
            val email = currentUser.email ?: "sofia@email.com · Sonsonate"
            _userName.value = name
            _userEmail.value = "$email · Sonsonate"
            _userInitial.value = name.take(1).uppercase()
        } else {
            _userName.value = "Usuario Invitado"
            _userEmail.value = "Modo Exploración · Sonsonate"
            _userInitial.value = "I"
            _savedCount.value = "0"
            _reviewsCount.value = "0"
            _visitsCount.value = "0"
        }
    }

    fun signOut() {
        auth.signOut()
    }
}