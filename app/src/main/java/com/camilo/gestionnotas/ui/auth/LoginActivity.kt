package com.camilo.gestionnotas.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.camilo.gestionnotas.R
import com.camilo.gestionnotas.ui.notes.MainWindow
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()


        val emailEditText = findViewById<EditText>(R.id.emailLoginEditText)
        val passwordEditText = findViewById<EditText>(R.id.contraLoginEditText)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val googleLoginImageView = findViewById<ImageView>(R.id.googleLoginImageView)
        val registerTextView = findViewById<TextView>(R.id.registerTextView)

        // Configurar el listener para el botón de login
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            // Validar los campos
            if (email.isEmpty()) {
                emailEditText.error = "Por favor, ingresa un correo electrónico"
                emailEditText.requestFocus()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailEditText.error = "Por favor, ingresa un correo válido"
                emailEditText.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                passwordEditText.error = "Por favor, ingresa una contraseña"
                passwordEditText.requestFocus()
                return@setOnClickListener
            }

            if (password.length < 6) {
                passwordEditText.error = "La contraseña debe tener al menos 6 caracteres"
                passwordEditText.requestFocus()
                return@setOnClickListener
            }

            // Iniciar sesión con Firebase Authentication
            loginUser(email, password)
        }

        // Listener para el registro (navegar a la pantalla de registro)
        registerTextView.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        // Listener para el login con Google (agregar lógica más adelante)
        googleLoginImageView.setOnClickListener {
            // Implementa el login con Google aquí (requiere configuración adicional)
            Toast.makeText(this, "Login con Google no implementado", Toast.LENGTH_SHORT).show()
        }




    }
    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Si el login es exitoso, navega a la actividad principal
                    val intent = Intent(this, MainWindow::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(
                        this,
                        "Error en la autenticación: ${task.exception?.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }
}


