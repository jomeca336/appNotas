package com.camilo.gestionnotas.ui.notes

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.StateListDrawable
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.camilo.gestionnotas.databinding.ActivityCreateNoteBinding
import com.camilo.gestionnotas.ui.provider.NoteProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CreateNote : AppCompatActivity() {

    // Referencias a Firebase
    lateinit var biding: ActivityCreateNoteBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var mNoteProvider:NoteProvider
    var backgroundColor= "#CFCFCF"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        biding = ActivityCreateNoteBinding.inflate(layoutInflater)
        setContentView(biding.root)
        mNoteProvider= NoteProvider()


        mAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
        var background: StateListDrawable


        biding.grayColorSelected.setOnClickListener {
            backgroundColor = "#CFCFCF"
            background = biding.titleEditText.background.mutate() as StateListDrawable
            background.setTint(Color.parseColor(backgroundColor))
            background = biding.descriptionEditText.background.mutate() as StateListDrawable
            background.setTint(Color.parseColor(backgroundColor))
        }
        biding.yellowColorSelected.setOnClickListener {
            backgroundColor = "#FFC107"
            background = biding.titleEditText.background.mutate() as StateListDrawable
            background.setTint(Color.parseColor(backgroundColor))
            background = biding.descriptionEditText.background.mutate() as StateListDrawable
            background.setTint(Color.parseColor(backgroundColor))
        }
        biding.blueColorSelected.setOnClickListener {
            backgroundColor = "#03A9F4"
            background = biding.titleEditText.background.mutate() as StateListDrawable
            background.setTint(Color.parseColor(backgroundColor))
            background = biding.descriptionEditText.background.mutate() as StateListDrawable
            background.setTint(Color.parseColor(backgroundColor))
        }
        biding.greenColorSelected.setOnClickListener {
            backgroundColor = "#4CAF50"
            background = biding.titleEditText.background.mutate() as StateListDrawable
            background.setTint(Color.parseColor(backgroundColor))
            background = biding.descriptionEditText.background.mutate() as StateListDrawable
            background.setTint(Color.parseColor(backgroundColor))
        }


        biding.backButton.setOnClickListener {
            val intent = Intent(this, MainWindow::class.java)
            startActivity(intent)
            finish()
        }

        biding.createButton.setOnClickListener {
            val title = biding.titleEditText.text.toString().trim()
            val description = biding.descriptionEditText.text.toString().trim()


            if (title.isEmpty()) {
                biding.titleEditText.error = "Por favor, ingresa un título"
                biding.titleEditText.requestFocus()
                return@setOnClickListener
            }

            if (description.isEmpty()) {
                biding.descriptionEditText.error = "Por favor, ingresa una descripción"
                biding.descriptionEditText.requestFocus()
                return@setOnClickListener
            }

            createNote(title, description)
        }
    }


    private fun createNote(title: String, description: String) {
        val note = Note(
            title = title,
            description = description,
            userId = mAuth.uid.toString(),
            timestamp = System.currentTimeMillis(),
            backgroundColor = backgroundColor,
            favorite = false
        )
        mNoteProvider.createNote(note).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(this, "Nota creada correctamente", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainWindow::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
                // Cierra la actividad después de crear la nota
            } else {
                Toast.makeText(this, "Error al crear la nota", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainWindow::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()  // Cierra la actividad actual para evitar que permanezca en el stack
    }


}