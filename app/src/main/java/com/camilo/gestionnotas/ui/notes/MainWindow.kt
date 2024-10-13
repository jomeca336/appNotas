package com.camilo.gestionnotas.ui.notes


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.camilo.gestionnotas.R
import com.camilo.gestionnotas.ui.fragments.FavoritesFragment
import com.camilo.gestionnotas.ui.fragments.HomeFragment
import com.camilo.gestionnotas.ui.fragments.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainWindow : AppCompatActivity() {

    private lateinit var createButton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_window)

        // Configurar el BottomNavigationView
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)
        createButton = findViewById(R.id.createButton)

        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home -> {
                    loadFragment(HomeFragment())  // Cargar HomeFragment
                    showFloatingActionButton(true)  // Mostrar botón flotante solo en Home
                    true
                }
                R.id.menu_favorites -> {
                    loadFragment(FavoritesFragment())  // Cargar FavoritesFragment
                    showFloatingActionButton(false)  // Ocultar botón flotante
                    true
                }
                R.id.menu_profile -> {
                    loadFragment(ProfileFragment())  // Cargar ProfileFragment
                    showFloatingActionButton(false)  // Ocultar botón flotante
                    true
                }
                else -> false
            }
        }

        // Cargar el HomeFragment como fragmento inicial
        if (savedInstanceState == null) {
            loadFragment(HomeFragment())
            showFloatingActionButton(true)  // Mostrar el botón flotante en el inicio
        }

        // Configurar el FloatingActionButton para crear nuevas notas
        createButton.setOnClickListener {
            // Abrir la actividad para crear una nueva nota
            val intent = Intent(this, CreateNote::class.java)
            startActivity(intent)
        }
    }

    // Método para cargar fragmentos
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }



    // Método para mostrar u ocultar el FloatingActionButton
    private fun showFloatingActionButton(show: Boolean) {
        if (show) {
            createButton.show()  // Mostrar el botón flotante
        } else {
            createButton.hide()  // Ocultar el botón flotante
        }
    }
}