package com.example.poraqui.ui.map

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
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
import com.example.poraqui.data.model.Business
import com.example.poraqui.ui.explore.ExploreActivity
import com.example.poraqui.viewmodel.MapViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.json.JSONArray
import org.json.JSONObject

class MapActivity : AppCompatActivity() {

    private val viewModel: MapViewModel by viewModels()
    private lateinit var pinAdapter: MapPinAdapter
    private lateinit var webViewMap: WebView
    private var isMapLoaded = false

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
        setupWebView()
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

        val rvPins = findViewById<RecyclerView>(R.id.rv_map_pins)
        pinAdapter = MapPinAdapter { business ->
            viewModel.selectBusiness(business)
        }
        rvPins.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvPins.adapter = pinAdapter
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        webViewMap = findViewById(R.id.web_view_map)
        webViewMap.settings.javaScriptEnabled = true
        webViewMap.settings.domStorageEnabled = true
        webViewMap.addJavascriptInterface(WebAppInterface(), "AndroidBridge")

        webViewMap.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                isMapLoaded = true
                viewModel.businesses.value?.let { list ->
                    sendBusinessesToMap(list)
                }
            }
        }

        webViewMap.loadUrl("file:///android_asset/leaflet_map.html")
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

        viewModel.businesses.observe(this) { list ->
            pinAdapter.updateList(list)
            if (isMapLoaded) {
                sendBusinessesToMap(list)
            }
        }

        viewModel.selectedBusiness.observe(this) { business ->
            tvIcon.text = business.iconEmoji
            tvName.text = business.name
            tvRatingCategory.text = "★ ${business.rating} · ${business.category}"
            val coordsText = if (business.latitude != 0.0 && business.longitude != 0.0) {
                "📍 ${business.location} (${String.format("%.4f", business.latitude)}, ${String.format("%.4f", business.longitude)})"
            } else {
                "📍 ${business.distance} · ${business.location}"
            }
            tvLocation.text = coordsText

            if (business.isOpen) {
                tvStatus.text = "• Abierto"
                tvStatus.setTextColor(Color.parseColor("#00FF87"))
            } else {
                tvStatus.text = "• Cerrado"
                tvStatus.setTextColor(Color.parseColor("#F59E0B"))
            }
        }
    }

    private fun sendBusinessesToMap(list: List<Business>) {
        val jsonArray = JSONArray()
        for (b in list) {
            val obj = JSONObject()
            obj.put("id", b.id)
            obj.put("name", b.name)
            obj.put("category", b.category)
            obj.put("location", b.location)
            obj.put("iconEmoji", b.iconEmoji)
            obj.put("isOpen", b.isOpen)
            obj.put("latitude", b.latitude)
            obj.put("longitude", b.longitude)
            jsonArray.put(obj)
        }

        val jsonString = jsonArray.toString().replace("'", "\\'")
        webViewMap.post {
            webViewMap.evaluateJavascript("setBusinesses('$jsonString')", null)
        }
    }

    inner class WebAppInterface {
        @JavascriptInterface
        fun onBusinessSelected(id: String) {
            runOnUiThread {
                viewModel.selectBusinessById(id)
            }
        }
    }
}