package com.example.poraqui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) : ViewModel() {

    sealed class LoginState {
        object Idle : LoginState()
        object Loading : LoginState()
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

        _loginState.value = LoginState.Loading

        auth.signInWithEmailAndPassword(trimmedEmail, trimmedPass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _loginState.value = LoginState.Success("¡Bienvenido a PorAquí!")
                } else {
                    val exceptionMsg = task.exception?.message.orEmpty()
                    val errorMessage = if (exceptionMsg.contains("CONFIGURATION_NOT_FOUND")) {
                        "Falta configurar Firebase: Habilita Email/Password en Firebase Console y descarga google-services.json"
                    } else {
                        task.exception?.localizedMessage ?: "Error al iniciar sesión en Firebase"
                    }
                    _loginState.value = LoginState.Error(errorMessage)
                }
            }
    }

    fun resetState() {
        _loginState.value = LoginState.Idle
    }
}