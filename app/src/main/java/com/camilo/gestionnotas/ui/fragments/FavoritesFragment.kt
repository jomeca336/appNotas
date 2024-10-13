package com.camilo.gestionnotas.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.camilo.gestionnotas.R
import com.camilo.gestionnotas.ui.adapter.AdapterNote
import com.camilo.gestionnotas.ui.notes.Note
import com.camilo.gestionnotas.ui.provider.NoteProvider
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth

class FavoritesFragment : Fragment() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mNoteProvider: NoteProvider
    private lateinit var noteAdapter: AdapterNote
    private lateinit var favoritesRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar el layout del fragmento
        val view = inflater.inflate(R.layout.fragment_favorites, container, false)

        // Inicializa Firestore y Firebase Auth
        mAuth = FirebaseAuth.getInstance()
        mNoteProvider = NoteProvider()

        val noFavoritesTextView = view.findViewById<TextView>(R.id.noFavoritesTextView)

        // Configura el RecyclerView
        favoritesRecyclerView = view.findViewById(R.id.favoritesRecyclerView)
        favoritesRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Configura el adaptador para las notas marcadas como favoritas
        val query = mNoteProvider.getNoteByUserAndFavorite(mAuth.uid.toString(),true)
        val options = FirestoreRecyclerOptions.Builder<Note>()
            .setQuery(query, Note::class.java)
            .build()

        noteAdapter = AdapterNote(options, requireContext())
        query.get().addOnSuccessListener {
            val noteCount = it.size()
            if (noteCount == 0) {
                view.findViewById<View>(R.id.noFavoritesTextView).visibility = View.VISIBLE
            }else {
                view.findViewById<View>(R.id.noFavoritesTextView).visibility = View.GONE

                favoritesRecyclerView.adapter = noteAdapter
            }
        }


        return view
    }

    override fun onStart() {
        super.onStart()
        noteAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        noteAdapter.stopListening()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Limpia el adaptador del RecyclerView para evitar referencias a vistas viejas
        favoritesRecyclerView.adapter = null
    }

}
