package com.camilo.gestionnotas.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.camilo.gestionnotas.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class RegisterActivity : AppCompatActivity() {
    // Instancia de FirebaseAuth
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register) // Asegúrate de que el nombre del layout coincida

        // Inicializar FirebaseAuth
        auth = FirebaseAuth.getInstance()

        // Obtener referencias de los elementos del layout
        val nameEditText = findViewById<EditText>(R.id.nameRegisterEditText)
        val emailEditText = findViewById<EditText>(R.id.emailRegisterEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordRegisterEditText)
        val registerButton = findViewById<Button>(R.id.finishRegisterButton)

        // Configurar el listener para el botón de registro
        registerButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            // Validar los campos
            if (name.isEmpty()) {
                nameEditText.error = "Por favor, ingresa tu nombre"
                nameEditText.requestFocus()
                return@setOnClickListener
            }

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

            // Registrar el usuario en Firebase Authentication
            registerUser(name, email, password)
        }
    }

    // Función para registrar el usuario
    private fun registerUser(name: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Actualizar el perfil del usuario con su nombre
                    val user = auth.currentUser
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build()

                    user?.updateProfile(profileUpdates)
                        ?.addOnCompleteListener { profileTask ->
                            if (profileTask.isSuccessful) {
                                Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                                // Navegar a la actividad principal o de login
                                val intent = Intent(this, LoginActivity::class.java)
                                startActivity(intent)
                                finish() // Finalizar la actividad actual para que no se pueda volver
                            } else {
                                Toast.makeText(this, "Error al actualizar el perfil", Toast.LENGTH_LONG).show()
                            }
                        }
                } else {
                    // Si el registro falla, mostrar un mensaje de error
                    Toast.makeText(this, "Error en el registro: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }
}