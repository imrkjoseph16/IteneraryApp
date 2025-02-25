package com.example.iteneraryapplication.dashboard.shared.domain.data

import com.example.iteneraryapplication.dashboard.shared.data.Expenses

data class Notes(
    val itemId: String? = null,
    val notesTitle: String? = null,
    val notesDateSaved: String? = null,
    val notesSubtitle: String? = null,
    val notesColor: String = "#333333",
    val notesImage: String? = null,
    val notesWebLink: String? = null,
    val notesDesc: String? = null,
    val listOfExpenses: MutableList<Expenses>? = null,
    val canPinnedNote: Boolean = false
)