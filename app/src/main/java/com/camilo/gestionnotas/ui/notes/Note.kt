package com.camilo.gestionnotas.ui.notes

data class Note(
    var id: String = "",
    var title: String = "",
    var description: String = "",
    var userId: String = "",
    var timestamp: Long = 0,
    var backgroundColor: String = "",
    var favorite: Boolean = false
)
