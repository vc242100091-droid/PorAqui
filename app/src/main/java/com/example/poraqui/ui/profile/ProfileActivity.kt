package com.example.poraqui.ui.profile

import android.content.Intent
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
import com.example.poraqui.ui.auth.WelcomeActivity
import com.example.poraqui.ui.explore.ExploreActivity
import com.example.poraqui.ui.map.MapActivity
import com.example.poraqui.viewmodel.ProfileViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProfileActivity : AppCompatActivity() {

    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.profile_main)) { v, insets ->
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
        bottomNav.selectedItemId = R.id.nav_profile
    }

    private fun setupViews() {
        val btnBusinessMode = findViewById<TextView>(R.id.btn_business_mode)
        btnBusinessMode.setOnClickListener {
            Toast.makeText(this, "Modo Negocio (Próximamente)", Toast.LENGTH_SHORT).show()
        }

        val rowSaved = findViewById<LinearLayout>(R.id.row_saved_businesses)
        rowSaved.setOnClickListener {
            Toast.makeText(this, "Negocios guardados", Toast.LENGTH_SHORT).show()
        }

        val rowHistory = findViewById<LinearLayout>(R.id.row_history)
        rowHistory.setOnClickListener {
            Toast.makeText(this, "Historial de visitas", Toast.LENGTH_SHORT).show()
        }

        val rowNotifications = findViewById<LinearLayout>(R.id.row_notifications)
        rowNotifications.setOnClickListener {
            Toast.makeText(this, "Notificaciones", Toast.LENGTH_SHORT).show()
        }

        val rowSettings = findViewById<LinearLayout>(R.id.row_settings)
        rowSettings.setOnClickListener {
            Toast.makeText(this, "Configuración", Toast.LENGTH_SHORT).show()
        }

        val btnLogout = findViewById<TextView>(R.id.btn_logout)
        btnLogout.setOnClickListener {
            viewModel.signOut()
            val intent = Intent(this, WelcomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun setupNavigation() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.selectedItemId = R.id.nav_profile

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_profile -> true
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
                R.id.nav_map -> {
                    val intent = Intent(this, MapActivity::class.java)
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
        val tvName = findViewById<TextView>(R.id.tv_profile_name)
        val tvEmail = findViewById<TextView>(R.id.tv_profile_email)
        val tvAvatar = findViewById<TextView>(R.id.tv_profile_avatar)
        val tvSaved = findViewById<TextView>(R.id.tv_stat_saved)
        val tvReviews = findViewById<TextView>(R.id.tv_stat_reviews)
        val tvVisits = findViewById<TextView>(R.id.tv_stat_visits)

        viewModel.userName.observe(this) { name ->
            tvName.text = name
        }

        viewModel.userEmail.observe(this) { email ->
            tvEmail.text = email
        }

        viewModel.userInitial.observe(this) { initial ->
            tvAvatar.text = initial
        }

        viewModel.savedCount.observe(this) { count ->
            tvSaved.text = count
        }

        viewModel.reviewsCount.observe(this) { count ->
            tvReviews.text = count
        }

        viewModel.visitsCount.observe(this) { count ->
            tvVisits.text = count
        }
    }
}