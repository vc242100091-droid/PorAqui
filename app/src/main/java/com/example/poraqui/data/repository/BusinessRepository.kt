package com.example.poraqui.data.repository

import com.example.poraqui.data.model.Business
import com.example.poraqui.data.model.Category
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class BusinessRepository {

    private val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

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
                iconBgColorHex = "#FDE68A",
                latitude = 13.7188,
                longitude = -89.7242
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
                iconBgColorHex = "#FDE68A",
                latitude = 13.7210,
                longitude = -89.7215
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
                iconBgColorHex = "#C7D2FE",
                latitude = 13.7155,
                longitude = -89.7280
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
                iconBgColorHex = "#A7F3D0",
                latitude = 13.7230,
                longitude = -89.7200
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
                iconBgColorHex = "#C7D2FE",
                latitude = 13.7120,
                longitude = -89.7265
            )
        )
    }

    fun getBusinessesFromFirestore(onResult: (List<Business>) -> Unit) {
        val staticList = getNearbyBusinesses()
        firestore.collection("negocios")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val firestoreBusinesses = mutableListOf<Business>()
                for (doc in querySnapshot.documents) {
                    val id = doc.getString("id") ?: doc.id
                    val name = doc.getString("name") ?: "Sin nombre"
                    val category = doc.getString("category") ?: "Otros"
                    val rating = doc.getDouble("rating") ?: 5.0
                    val distance = doc.getString("distance") ?: "500 m"
                    val location = doc.getString("location") ?: "Sonsonate"
                    val isOpen = doc.getBoolean("isOpen") ?: true
                    val iconEmoji = doc.getString("iconEmoji") ?: "🏪"
                    val lat = doc.getDouble("latitude") ?: 13.7188
                    val lng = doc.getDouble("longitude") ?: -89.7242

                    firestoreBusinesses.add(
                        Business(
                            id = id,
                            name = name,
                            category = category,
                            rating = rating,
                            distance = distance,
                            location = location,
                            isOpen = isOpen,
                            iconEmoji = iconEmoji,
                            isDemo = false,
                            latitude = lat,
                            longitude = lng
                        )
                    )
                }

                val fullList = mutableListOf<Business>()
                fullList.addAll(firestoreBusinesses)
                fullList.addAll(staticList)
                onResult(fullList)
            }
            .addOnFailureListener {
                onResult(staticList)
            }
    }

    fun listenBusinessesFromFirestore(onUpdate: (List<Business>) -> Unit) {
        val staticList = getNearbyBusinesses()
        firestore.collection("negocios")
            .addSnapshotListener { querySnapshot, error ->
                if (error != null || querySnapshot == null) {
                    onUpdate(staticList)
                    return@addSnapshotListener
                }

                val firestoreBusinesses = mutableListOf<Business>()
                for (doc in querySnapshot.documents) {
                    val id = doc.getString("id") ?: doc.id
                    val name = doc.getString("name") ?: "Sin nombre"
                    val category = doc.getString("category") ?: "Otros"
                    val rating = doc.getDouble("rating") ?: 5.0
                    val distance = doc.getString("distance") ?: "500 m"
                    val location = doc.getString("location") ?: "Sonsonate"
                    val isOpen = doc.getBoolean("isOpen") ?: true
                    val iconEmoji = doc.getString("iconEmoji") ?: "🏪"
                    val lat = doc.getDouble("latitude") ?: 13.7188
                    val lng = doc.getDouble("longitude") ?: -89.7242

                    firestoreBusinesses.add(
                        Business(
                            id = id,
                            name = name,
                            category = category,
                            rating = rating,
                            distance = distance,
                            location = location,
                            isOpen = isOpen,
                            iconEmoji = iconEmoji,
                            isDemo = false,
                            latitude = lat,
                            longitude = lng
                        )
                    )
                }

                val fullList = mutableListOf<Business>()
                fullList.addAll(firestoreBusinesses)
                fullList.addAll(staticList)
                onUpdate(fullList)
            }
    }

    fun saveBusiness(
        name: String,
        category: String,
        location: String,
        iconEmoji: String,
        isOpen: Boolean,
        latitude: Double,
        longitude: Double,
        ownerUid: String?,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val docRef = firestore.collection("negocios").document()
        val businessData = hashMapOf(
            "id" to docRef.id,
            "name" to name,
            "category" to category,
            "location" to location,
            "iconEmoji" to if (iconEmoji.isBlank()) "🏪" else iconEmoji,
            "isOpen" to isOpen,
            "rating" to 5.0,
            "distance" to "500 m",
            "latitude" to latitude,
            "longitude" to longitude,
            "ownerUid" to ownerUid.orEmpty(),
            "createdAt" to FieldValue.serverTimestamp()
        )

        docRef.set(businessData)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onError(e.localizedMessage ?: "Error al guardar el negocio en Firebase") }
    }
}