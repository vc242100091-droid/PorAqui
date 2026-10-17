package com.example.poraqui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel : ViewModel() {

    private val _navigateToNext = MutableLiveData<Boolean>()
    val navigateToNext: LiveData<Boolean> get() = _navigateToNext

    private val _statusText = MutableLiveData<String>("Iniciando experiencia...")
    val statusText: LiveData<String> get() = _statusText

    init {
        startSplashSequence()
    }

    private fun startSplashSequence() {
        viewModelScope.launch {
            delay(1000)
            _statusText.value = "Conectando con tu comunidad..."
            delay(1200)
            _statusText.value = "Sintonizando negocios locales..."
            delay(1200)
            _navigateToNext.value = true
        }
    }
}