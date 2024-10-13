package com.camilo.gestionnotas.ui.provider

import android.util.Log
import com.camilo.gestionnotas.ui.notes.Note
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class NoteProvider {

    private var collection:CollectionReference = FirebaseFirestore.getInstance().collection("notes")

    fun createNote(note: Note): Task<Void> {
        var id= collection.document().id
        note.id=id
        Log.d("idNose", id)
        Log.d("noteNose", note.description)
        return collection.document(id).set(note)
    }

    fun getAllNotes():CollectionReference{
        return collection
    }

    fun getNotesByUser(userId:String): Query {
        return collection.whereEqualTo("userId", userId)
    }

    fun getNoteByUserAndFavorite(userId:String, favorite:Boolean):Query{
        return collection.whereEqualTo("userId", userId).whereEqualTo("favorite", favorite)
    }


    fun setFavorite(id:String):Task<Void>{
        Log.d("favorito", id)
        return collection.document(id).update("favorite", true)
    }

    fun removeFavorite(id:String):Task<Void>{
        Log.d("favoritono", id)
       return collection.document(id).update("favorite", false)
    }

    fun deleteNote(id:String):Task<Void>{
        return collection.document(id).delete()
    }

}