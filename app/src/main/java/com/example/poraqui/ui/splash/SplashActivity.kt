package com.example.poraqui.ui.splash

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.poraqui.R
import com.example.poraqui.ui.auth.WelcomeActivity
import com.example.poraqui.viewmodel.SplashViewModel

class SplashActivity : AppCompatActivity() {

    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.splash_main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupViews()
        observeViewModel()
    }

    private fun setupViews() {
        val tvTitle = findViewById<TextView>(R.id.tv_splash_title)
        val sloganText = "Descubre lo que<br><font color='#4EDEA3'>hay cerca de ti</font>"
        tvTitle.text = HtmlCompat.fromHtml(sloganText, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

    private fun observeViewModel() {
        val tvStatus = findViewById<TextView>(R.id.tv_splash_status)

        viewModel.statusText.observe(this) { status ->
            tvStatus.text = status
        }

        viewModel.navigateToNext.observe(this) { shouldNavigate ->
            if (shouldNavigate) {
                navigateToNextScreen()
            }
        }
    }

    private fun navigateToNextScreen() {
        startActivity(Intent(this, WelcomeActivity::class.java))
        finish()
    }
}