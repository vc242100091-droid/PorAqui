package com.example.poraqui

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.poraqui.ui.home.BusinessAdapter
import com.example.poraqui.ui.home.CategoryAdapter
import com.example.poraqui.viewmodel.MainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var businessAdapter: BusinessAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
            bottomNav.setPadding(0, 6, 0, systemBars.bottom + 6)
            insets
        }

        setupRecyclerViews()
        setupSearch()
        setupNavigation()
        observeViewModel()
    }

    override fun onResume() {
        super.onResume()
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.selectedItemId = R.id.nav_home
    }

    private fun setupRecyclerViews() {
        val rvCategories = findViewById<RecyclerView>(R.id.rv_categories)
        val rvBusinesses = findViewById<RecyclerView>(R.id.rv_businesses)

        categoryAdapter = CategoryAdapter { category ->
            val intent = Intent(this, com.example.poraqui.ui.explore.ExploreActivity::class.java)
            startActivity(intent)
        }
        rvCategories.layoutManager = GridLayoutManager(this, 4)
        rvCategories.adapter = categoryAdapter

        businessAdapter = BusinessAdapter { business ->
            Toast.makeText(this, "Seleccionado: ${business.name}", Toast.LENGTH_SHORT).show()
        }
        rvBusinesses.layoutManager = LinearLayoutManager(this)
        rvBusinesses.adapter = businessAdapter
    }

    private fun setupSearch() {
        val etSearch = findViewById<EditText>(R.id.et_search)
        val btnSearch = findViewById<TextView>(R.id.btn_search)

        btnSearch.setOnClickListener {
            val query = etSearch.text.toString()
            viewModel.filterBusinesses(query)
        }
    }

    private fun setupNavigation() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.selectedItemId = R.id.nav_home

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> true
                R.id.nav_explore -> {
                    startActivity(Intent(this, com.example.poraqui.ui.explore.ExploreActivity::class.java))
                    true
                }
                R.id.nav_map -> {
                    startActivity(Intent(this, com.example.poraqui.ui.map.MapActivity::class.java))
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

        val tvCategoriesAll = findViewById<TextView>(R.id.tv_categories_all)
        tvCategoriesAll.setOnClickListener {
            startActivity(Intent(this, com.example.poraqui.ui.explore.ExploreActivity::class.java))
        }

        val tvSeeMap = findViewById<TextView>(R.id.tv_see_map)
        tvSeeMap.setOnClickListener {
            startActivity(Intent(this, com.example.poraqui.ui.map.MapActivity::class.java))
        }
    }

    private fun observeViewModel() {
        val tvUserName = findViewById<TextView>(R.id.tv_user_name)
        val tvAvatar = findViewById<TextView>(R.id.tv_avatar)

        viewModel.userName.observe(this) { name ->
            tvUserName.text = "$name 👋"
        }

        viewModel.userInitial.observe(this) { initial ->
            tvAvatar.text = initial
        }

        viewModel.categories.observe(this) { categories ->
            categoryAdapter.updateList(categories)
        }

        viewModel.businesses.observe(this) { businesses ->
            businessAdapter.updateList(businesses)
        }
    }
}