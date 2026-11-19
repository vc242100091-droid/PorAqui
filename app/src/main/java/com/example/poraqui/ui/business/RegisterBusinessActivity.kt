package com.example.poraqui.ui.business

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ScrollView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.poraqui.MainActivity
import com.example.poraqui.R
import com.example.poraqui.viewmodel.RegisterBusinessViewModel

class RegisterBusinessActivity : AppCompatActivity() {

    private val viewModel: RegisterBusinessViewModel by viewModels()
    private lateinit var webViewPicker: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register_business)

        val scrollView = findViewById<ScrollView>(R.id.scroll_view_register_business)
        ViewCompat.setOnApplyWindowInsetsListener(scrollView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupViews()
        setupPickerWebView()
        observeViewModel()
    }

    private fun setupViews() {
        val btnBack = findViewById<TextView>(R.id.btn_back_business)
        val etName = findViewById<EditText>(R.id.et_business_name)
        val spCategory = findViewById<Spinner>(R.id.sp_business_category)
        val etLocation = findViewById<EditText>(R.id.et_business_location)
        val etEmoji = findViewById<EditText>(R.id.et_business_emoji)
        val etLatitude = findViewById<EditText>(R.id.et_business_latitude)
        val etLongitude = findViewById<EditText>(R.id.et_business_longitude)
        val cbOpen = findViewById<CheckBox>(R.id.cb_business_open)
        val btnSubmit = findViewById<Button>(R.id.btn_register_business)

        btnBack.setOnClickListener {
            finish()
        }

        val categoryList = viewModel.getCategories()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categoryList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spCategory.adapter = adapter

        btnSubmit.setOnClickListener {
            val name = etName.text.toString()
            val location = etLocation.text.toString()
            val emoji = etEmoji.text.toString()
            val latitudeStr = etLatitude.text.toString()
            val longitudeStr = etLongitude.text.toString()
            val isOpen = cbOpen.isChecked
            val selectedCategoryPos = spCategory.selectedItemPosition

            viewModel.submitBusiness(
                name = name,
                selectedCategoryIndex = selectedCategoryPos,
                categories = categoryList,
                location = location,
                emoji = emoji,
                isOpen = isOpen,
                latitudeStr = latitudeStr,
                longitudeStr = longitudeStr
            )
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupPickerWebView() {
        webViewPicker = findViewById(R.id.web_view_location_picker)
        webViewPicker.settings.javaScriptEnabled = true
        webViewPicker.settings.domStorageEnabled = true
        webViewPicker.addJavascriptInterface(PickerBridge(), "AndroidBridge")
        webViewPicker.loadUrl("file:///android_asset/leaflet_picker.html")
    }

    private fun observeViewModel() {
        val btnSubmit = findViewById<Button>(R.id.btn_register_business)

        viewModel.state.observe(this) { state ->
            when (state) {
                is RegisterBusinessViewModel.State.Idle -> {
                    btnSubmit.isEnabled = true
                }
                is RegisterBusinessViewModel.State.Loading -> {
                    btnSubmit.isEnabled = false
                }
                is RegisterBusinessViewModel.State.Success -> {
                    btnSubmit.isEnabled = true
                    Toast.makeText(this, state.message, Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivity(intent)
                    finish()
                }
                is RegisterBusinessViewModel.State.Error -> {
                    btnSubmit.isEnabled = true
                    Toast.makeText(this, state.message, Toast.LENGTH_LONG).show()
                    viewModel.resetState()
                }
            }
        }
    }

    inner class PickerBridge {
        @JavascriptInterface
        fun onLocationSelected(lat: String, lng: String) {
            runOnUiThread {
                val etLatitude = findViewById<EditText>(R.id.et_business_latitude)
                val etLongitude = findViewById<EditText>(R.id.et_business_longitude)
                etLatitude.setText(lat)
                etLongitude.setText(lng)
            }
        }
    }
}