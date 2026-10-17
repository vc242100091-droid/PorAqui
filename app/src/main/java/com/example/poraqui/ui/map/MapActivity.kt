package com.example.poraqui.ui.map

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.poraqui.MainActivity
import com.example.poraqui.R
import com.example.poraqui.ui.explore.ExploreActivity
import com.example.poraqui.viewmodel.MapViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class MapActivity : AppCompatActivity() {

    private val viewModel: MapViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_map)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.map_main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
            bottomNav.setPadding(0, 6, 0, systemBars.bottom + 6)
            insets
        }

        setupViews()
        setupNavigation()
        observeViewModel()
    }

    override fun onResume() {
        super.onResume()
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.selectedItemId = R.id.nav_map
    }

    private fun setupViews() {
        val btnFilter = findViewById<TextView>(R.id.btn_map_filter)
        btnFilter.setOnClickListener {
            Toast.makeText(this, "Filtros de mapa (Radio: 3.5 km)", Toast.LENGTH_SHORT).show()
        }

        val pinCoffee = findViewById<LinearLayout>(R.id.pin_coffee)
        pinCoffee.setOnClickListener {
            viewModel.selectBusinessByName("Cafetería La Esquina")
        }

        val pinMarta = findViewById<LinearLayout>(R.id.pin_marta)
        pinMarta.setOnClickListener {
            viewModel.selectBusinessByName("Pupusería Doña Marta")
        }
    }

    private fun setupNavigation() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.selectedItemId = R.id.nav_map

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_map -> true
                R.id.nav_home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.nav_explore -> {
                    val intent = Intent(this, ExploreActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.nav_profile -> {
                    val intent = Intent(this, com.example.poraqui.ui.profile.ProfileActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivity(intent)
                    finish()
                    true
                }
                else -> false
            }
        }
    }

    private fun observeViewModel() {
        val tvHeaderTitle = findViewById<TextView>(R.id.tv_map_header_title)
        val tvRadius = findViewById<TextView>(R.id.tv_map_radius)

        val tvIcon = findViewById<TextView>(R.id.tv_selected_icon)
        val tvName = findViewById<TextView>(R.id.tv_selected_name)
        val tvRatingCategory = findViewById<TextView>(R.id.tv_selected_rating_category)
        val tvLocation = findViewById<TextView>(R.id.tv_selected_location)
        val tvStatus = findViewById<TextView>(R.id.tv_selected_status)

        viewModel.mapHeaderTitle.observe(this) { title ->
            tvHeaderTitle.text = title
        }

        viewModel.radiusText.observe(this) { radius ->
            tvRadius.text = radius
        }

        viewModel.selectedBusiness.observe(this) { business ->
            tvIcon.text = business.iconEmoji
            tvName.text = business.name
            tvRatingCategory.text = "★ ${business.rating} · ${business.category}"
            tvLocation.text = "📍 ${business.distance} · ${business.location}"

            if (business.isOpen) {
                tvStatus.text = "• Abierto"
                tvStatus.setTextColor(Color.parseColor("#00FF87"))
            } else {
                tvStatus.text = "• Cerrado"
                tvStatus.setTextColor(Color.parseColor("#F59E0B"))
            }
        }
    }
}