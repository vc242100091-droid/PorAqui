package com.example.poraqui.data.model

data class Business(
    val id: String,
    val name: String,
    val category: String,
    val rating: Double,
    val distance: String,
    val location: String,
    val isOpen: Boolean,
    val iconEmoji: String,
    val iconBgColorHex: String = "#FDE68A",
    val isDemo: Boolean = true
)