package com.camilo.gestionnotas.ui.start

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.camilo.gestionnotas.R
import com.camilo.gestionnotas.ui.auth.LoginActivity
import com.camilo.gestionnotas.ui.notes.MainWindow
import com.google.firebase.auth.FirebaseAuth

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_start)
        val mAuth = FirebaseAuth.getInstance()
        val splashTimeOut: Long = 3000

        Handler(Looper.getMainLooper()).postDelayed({
            if (mAuth.currentUser != null) {
                val intent = Intent(this, MainWindow::class.java)
                startActivity(intent)
                finish()
            }else{
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, splashTimeOut)
    }
}