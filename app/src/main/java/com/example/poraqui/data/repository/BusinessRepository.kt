package com.example.poraqui.data.repository

import com.example.poraqui.data.model.Business
import com.example.poraqui.data.model.Category

class BusinessRepository {

    fun getCategories(): List<Category> {
        return listOf(
            Category("1", "Comida", "🍔"),
            Category("2", "Cafeterías", "☕"),
            Category("3", "Ropa", "👗"),
            Category("4", "Belleza", "💇"),
            Category("5", "Servicios", "🔧"),
            Category("6", "Tiendas", "🛒"),
            Category("7", "Hospedaje", "🏨"),
            Category("8", "Otros", "✨")
        )
    }

    fun getNearbyBusinesses(): List<Business> {
        return listOf(
            Business(
                id = "1",
                name = "Cafetería La Esquina",
                category = "Cafeterías",
                rating = 4.8,
                distance = "850 m",
                location = "Centro de Sonsonate",
                isOpen = true,
                iconEmoji = "☕",
                iconBgColorHex = "#FDE68A"
            ),
            Business(
                id = "2",
                name = "Pupusería Doña Marta",
                category = "Comida",
                rating = 4.7,
                distance = "1.2 km",
                location = "Colonia El Ángel",
                isOpen = true,
                iconEmoji = "🍔",
                iconBgColorHex = "#FDE68A"
            ),
            Business(
                id = "3",
                name = "Estilo & Corte",
                category = "Belleza",
                rating = 4.6,
                distance = "1.7 km",
                location = "Avenida Morazán",
                isOpen = false,
                iconEmoji = "✂️",
                iconBgColorHex = "#C7D2FE"
            ),
            Business(
                id = "4",
                name = "Tienda El Ahorro",
                category = "Tiendas",
                rating = 4.8,
                distance = "2.1 km",
                location = "Barrio El Centro",
                isOpen = true,
                iconEmoji = "🛒",
                iconBgColorHex = "#A7F3D0"
            ),
            Business(
                id = "5",
                name = "Reparaciones Express",
                category = "Servicios",
                rating = 4.7,
                distance = "2.6 km",
                location = "Calle Principal",
                isOpen = true,
                iconEmoji = "🔧",
                iconBgColorHex = "#C7D2FE"
            )
        )
    }
}