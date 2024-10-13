package com.camilo.gestionnotas.ui.adapter

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.camilo.gestionnotas.R
import com.camilo.gestionnotas.ui.notes.MainWindow
import com.camilo.gestionnotas.ui.notes.Note
import com.camilo.gestionnotas.ui.provider.NoteProvider
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import java.util.Date
import java.util.Locale

class AdapterNote(options: FirestoreRecyclerOptions<Note>,val context: Context):
    FirestoreRecyclerAdapter<Note, AdapterNote.NoteViewHolder>(options) {
    var mNoteProvider = NoteProvider()

    class NoteViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val descriptionTextView: TextView = view.findViewById(R.id.descriptionTextView)
        val timestampTextView: TextView = view.findViewById(R.id.dateTextView)
        val noteCardView: LinearLayout = view.findViewById(R.id.noteLinearLayout)
        val favoriteImageView: ImageView = view.findViewById(R.id.isFavoriteImageView)
        val removeImageView: ImageView = view.findViewById(R.id.removeImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterNote.NoteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.note, parent, false)
        return AdapterNote.NoteViewHolder(view)


    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int, note: Note) {
        val titleTextView: TextView = holder.itemView.findViewById(R.id.titleTextView)
        Log.d("AdapterNote.onbid", "onBindViewHolder: $note")
        titleTextView.text = note.title
        holder.descriptionTextView.text = note.description
        holder.timestampTextView.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            .format(Date(note.timestamp))
        holder.noteCardView.setBackgroundColor(Color.parseColor(note.backgroundColor))
        if(note.favorite){
            holder.favoriteImageView.setImageResource(R.drawable.favorite_yes)
        }else{
            holder.favoriteImageView.setImageResource(R.drawable.favorite_no)
        }

        holder.favoriteImageView.setOnClickListener {
            if (note.favorite) {
                mNoteProvider.removeFavorite(note.id).addOnSuccessListener {
                    note.favorite = false
                    notifyItemChanged(position)
                }.addOnFailureListener {
                    Log.e("ErrorFav", "Error al quitar favorito", it)
                }
            } else {
                mNoteProvider.setFavorite(note.id).addOnSuccessListener {
                    note.favorite = true
                    notifyItemChanged(position)
                }.addOnFailureListener {
                    Log.e("ErrorFav", "Error al marcar favorito", it)
                }
            }
        }

        holder.removeImageView.setOnClickListener {

            val builder= AlertDialog.Builder(context)

            builder.setTitle("Confirmar eliminación")
            builder.setMessage("¿Estás seguro de que deseas eliminar esta nota?")

            builder.setPositiveButton("Sí") { dialog, which ->
                mNoteProvider.deleteNote(note.id).addOnSuccessListener {
                    Toast.makeText(context,"Nota eliminada",Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Log.e("ErrorRemove", "Error al eliminar nota", it)
                }
            }

            builder.setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
         }



    }


}