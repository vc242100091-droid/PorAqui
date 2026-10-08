package com.example.poraqui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

    sealed class LoginState {
        object Idle : LoginState()
        data class Success(val message: String) : LoginState()
        data class Error(val message: String) : LoginState()
    }

    private val _loginState = MutableLiveData<LoginState>(LoginState.Idle)
    val loginState: LiveData<LoginState> get() = _loginState

    fun onLoginClicked(email: String, pass: String) {
        val trimmedEmail = email.trim()
        val trimmedPass = pass.trim()

        if (trimmedEmail.isEmpty()) {
            _loginState.value = LoginState.Error("Ingresa tu correo electrónico")
            return
        }

        if (trimmedPass.isEmpty()) {
            _loginState.value = LoginState.Error("Ingresa tu contraseña")
            return
        }

        // Mock login for UI prototype
        _loginState.value = LoginState.Success("¡Bienvenido a PorAquí!")
    }

    fun resetState() {
        _loginState.value = LoginState.Idle
    }
}