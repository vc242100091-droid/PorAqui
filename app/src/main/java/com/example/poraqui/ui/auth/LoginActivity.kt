package com.example.poraqui.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.poraqui.MainActivity
import com.example.poraqui.R
import com.example.poraqui.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        val scrollView = findViewById<ScrollView>(R.id.scroll_view_login)
        ViewCompat.setOnApplyWindowInsetsListener(scrollView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupViews()
        observeViewModel()
    }

    private fun setupViews() {
        val tvRegister = findViewById<TextView>(R.id.tv_register)
        val btnLogin = findViewById<Button>(R.id.btn_login)
        val etEmail = findViewById<EditText>(R.id.et_email)
        val etPassword = findViewById<EditText>(R.id.et_password)
        val tvForgotPassword = findViewById<TextView>(R.id.tv_forgot_password)

        // Format Register Link text with colored "Regístrate"
        val registerText = "¿Aún no tienes cuenta? <font color='#0B8457'><b>Regístrate</b></font>"
        tvRegister.text = HtmlCompat.fromHtml(registerText, HtmlCompat.FROM_HTML_MODE_LEGACY)

        // Login Button Click
        btnLogin.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            viewModel.onLoginClicked(email, password)
        }

        // Forgot Password Click
        tvForgotPassword.setOnClickListener {
            Toast.makeText(this, "¿Olvidaste tu contraseña?", Toast.LENGTH_SHORT).show()
        }

        // Register Link Click
        tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun observeViewModel() {
        val btnLogin = findViewById<Button>(R.id.btn_login)
        viewModel.loginState.observe(this) { state ->
            when (state) {
                is LoginViewModel.LoginState.Loading -> {
                    btnLogin.isEnabled = false
                }
                is LoginViewModel.LoginState.Success -> {
                    btnLogin.isEnabled = true
                    Toast.makeText(this, state.message, Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                is LoginViewModel.LoginState.Error -> {
                    btnLogin.isEnabled = true
                    Toast.makeText(this, state.message, Toast.LENGTH_SHORT).show()
                    viewModel.resetState()
                }
                is LoginViewModel.LoginState.Idle -> {
                    btnLogin.isEnabled = true
                }
            }
        }
    }
}