package com.example.poraqui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.poraqui.data.repository.BusinessRepository
import com.google.firebase.auth.FirebaseAuth

class RegisterBusinessViewModel(
    private val repository: BusinessRepository = BusinessRepository(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) : ViewModel() {

    sealed class State {
        object Idle : State()
        object Loading : State()
        data class Success(val message: String) : State()
        data class Error(val message: String) : State()
    }

    private val _state = MutableLiveData<State>(State.Idle)
    val state: LiveData<State> get() = _state

    fun getCategories(): List<String> {
        return repository.getCategories().map { "${it.iconEmoji} ${it.name}" }
    }

    fun submitBusiness(
        name: String,
        selectedCategoryIndex: Int,
        categories: List<String>,
        location: String,
        emoji: String,
        isOpen: Boolean,
        latitudeStr: String,
        longitudeStr: String
    ) {
        val trimmedName = name.trim()
        val trimmedLocation = location.trim()

        if (trimmedName.isEmpty()) {
            _state.value = State.Error("Ingresa el nombre del negocio")
            return
        }

        if (trimmedLocation.isEmpty()) {
            _state.value = State.Error("Ingresa la dirección o ubicación del negocio")
            return
        }

        val categoryName = if (selectedCategoryIndex in categories.indices) {
            categories[selectedCategoryIndex].replace(Regex("^[\\p{L}\\p{N}\\p{P}\\p{Z}\\p{So}]+\\s+"), "")
                .ifEmpty { categories[selectedCategoryIndex] }
        } else {
            "Otros"
        }

        val finalCategory = if (categoryName.contains(" ")) {
            categoryName.split(" ").drop(1).joinToString(" ").ifEmpty { categoryName }
        } else categoryName

        val lat = latitudeStr.trim().toDoubleOrNull() ?: 13.7188
        val lng = longitudeStr.trim().toDoubleOrNull() ?: -89.7242

        _state.value = State.Loading

        val currentUid = auth.currentUser?.uid
        repository.saveBusiness(
            name = trimmedName,
            category = finalCategory,
            location = trimmedLocation,
            iconEmoji = emoji.trim(),
            isOpen = isOpen,
            latitude = lat,
            longitude = lng,
            ownerUid = currentUid,
            onSuccess = {
                _state.value = State.Success("¡Negocio registrado exitosamente!")
            },
            onError = { error ->
                _state.value = State.Error(error)
            }
        )
    }

    fun resetState() {
        _state.value = State.Idle
    }
}