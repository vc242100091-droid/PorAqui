package com.example.poraqui.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.poraqui.MainActivity
import com.example.poraqui.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    private var selectedAccountType = "usuario" // "usuario" or "negocio"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        val scrollView = findViewById<ScrollView>(R.id.scroll_view_register)
        ViewCompat.setOnApplyWindowInsetsListener(scrollView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupViews()
    }

    private fun setupViews() {
        val btnBack = findViewById<TextView>(R.id.btn_back_register)
        val cardUser = findViewById<LinearLayout>(R.id.card_type_user)
        val cardBusiness = findViewById<LinearLayout>(R.id.card_type_business)
        val indicatorUser = findViewById<TextView>(R.id.indicator_user)
        val indicatorBusiness = findViewById<TextView>(R.id.indicator_business)

        val etName = findViewById<EditText>(R.id.et_register_name)
        val etEmail = findViewById<EditText>(R.id.et_register_email)
        val etPassword = findViewById<EditText>(R.id.et_register_password)
        val btnSubmit = findViewById<Button>(R.id.btn_register_submit)
        val tvBackToLogin = findViewById<TextView>(R.id.tv_back_to_login)

        val loginLinkText = "¿Ya tienes cuenta? <font color='#00FF87'><b>Inicia sesión</b></font>"
        tvBackToLogin.text = HtmlCompat.fromHtml(loginLinkText, HtmlCompat.FROM_HTML_MODE_LEGACY)

        btnBack.setOnClickListener {
            finish()
        }

        cardUser.setOnClickListener {
            selectedAccountType = "usuario"
            indicatorUser.visibility = View.VISIBLE
            indicatorUser.setBackgroundResource(R.drawable.bg_avatar_circle)
            indicatorBusiness.visibility = View.INVISIBLE
            cardUser.background = getDrawable(R.drawable.bg_dark_card)
            cardBusiness.background = getDrawable(R.drawable.bg_dark_card)
        }

        cardBusiness.setOnClickListener {
            selectedAccountType = "negocio"
            indicatorBusiness.visibility = View.VISIBLE
            indicatorBusiness.setBackgroundResource(R.drawable.bg_avatar_circle)
            indicatorUser.visibility = View.INVISIBLE
            cardBusiness.background = getDrawable(R.drawable.bg_dark_card)
            cardUser.background = getDrawable(R.drawable.bg_dark_card)
        }

        btnSubmit.setOnClickListener {
            val fullName = etName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (fullName.isEmpty()) {
                Toast.makeText(this, "Ingresa tu nombre completo", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (email.isEmpty()) {
                Toast.makeText(this, "Ingresa un correo electrónico", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.length < 6) {
                Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            btnSubmit.isEnabled = false
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val firebaseUser = auth.currentUser
                        val uid = firebaseUser?.uid
                        if (uid != null) {
                            val userProfile = hashMapOf(
                                "uid" to uid,
                                "nombreCompleto" to fullName,
                                "correo" to email,
                                "tipoCuenta" to selectedAccountType,
                                "createdAt" to FieldValue.serverTimestamp()
                            )

                            firestore.collection("usuarios").document(uid).set(userProfile)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "¡Registro exitoso!", Toast.LENGTH_SHORT).show()
                                    startActivity(Intent(this, MainActivity::class.java))
                                    finishAffinity()
                                }
                                .addOnFailureListener { e ->
                                    btnSubmit.isEnabled = true
                                    Toast.makeText(
                                        this,
                                        "Cuenta creada, pero falló el perfil: ${e.localizedMessage}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                        } else {
                            btnSubmit.isEnabled = true
                            Toast.makeText(this, "Error al obtener UID de usuario", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        btnSubmit.isEnabled = true
                        val exceptionMsg = task.exception?.message.orEmpty()
                        val errorMessage = if (exceptionMsg.contains("CONFIGURATION_NOT_FOUND")) {
                            "Falta configurar Firebase: Habilita Email/Password en Firebase Console y descarga google-services.json"
                        } else {
                            task.exception?.localizedMessage ?: "Error en el registro"
                        }
                        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
                    }
                }
        }

        tvBackToLogin.setOnClickListener {
            finish()
        }
    }
}