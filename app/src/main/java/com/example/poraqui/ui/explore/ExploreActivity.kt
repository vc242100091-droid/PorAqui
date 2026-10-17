package com.example.poraqui.ui.explore

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.poraqui.MainActivity
import com.example.poraqui.R
import com.example.poraqui.ui.home.BusinessAdapter
import com.example.poraqui.viewmodel.ExploreViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class ExploreActivity : AppCompatActivity() {

    private val viewModel: ExploreViewModel by viewModels()

    private lateinit var filterAdapter: CategoryFilterAdapter
    private lateinit var businessAdapter: BusinessAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_explore)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.explore_main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
            bottomNav.setPadding(0, 6, 0, systemBars.bottom + 6)
            insets
        }

        setupViews()
        setupRecyclerViews()
        setupNavigation()
        observeViewModel()
    }

    override fun onResume() {
        super.onResume()
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.selectedItemId = R.id.nav_explore
    }

    private fun setupViews() {
        val btnBack = findViewById<TextView>(R.id.btn_back)
        btnBack.setOnClickListener {
            finish()
        }

        val etSearch = findViewById<EditText>(R.id.et_search_explore)
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.setSearchQuery(s?.toString() ?: "")
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setupRecyclerViews() {
        val rvFilters = findViewById<RecyclerView>(R.id.rv_category_filters)
        val rvBusinesses = findViewById<RecyclerView>(R.id.rv_explore_businesses)

        filterAdapter = CategoryFilterAdapter { chipItem ->
            viewModel.setSelectedCategory(chipItem.name)
        }
        rvFilters.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvFilters.adapter = filterAdapter

        businessAdapter = BusinessAdapter { business ->
            Toast.makeText(this, "Seleccionado: ${business.name}", Toast.LENGTH_SHORT).show()
        }
        rvBusinesses.layoutManager = LinearLayoutManager(this)
        rvBusinesses.adapter = businessAdapter
    }

    private fun setupNavigation() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.selectedItemId = R.id.nav_explore

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_explore -> true
                R.id.nav_home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.nav_map -> {
                    val intent = Intent(this, com.example.poraqui.ui.map.MapActivity::class.java)
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
        val tvResultsCount = findViewById<TextView>(R.id.tv_results_count)

        viewModel.filterChips.observe(this) { chips ->
            filterAdapter.updateList(chips)
        }

        viewModel.businesses.observe(this) { list ->
            businessAdapter.updateList(list)
        }

        viewModel.resultsCountText.observe(this) { text ->
            tvResultsCount.text = text
        }
    }
}