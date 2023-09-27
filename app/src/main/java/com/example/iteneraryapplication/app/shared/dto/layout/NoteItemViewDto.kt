package com.example.iteneraryapplication.app.shared.dto.layout

import android.content.Context
import com.example.iteneraryapplication.app.shared.component.TextLine
import com.example.iteneraryapplication.app.util.Default.Companion.DEFAULT_HTTPS_URL
import com.example.iteneraryapplication.app.util.Default.Companion.NOTES_DEFAULT_COLOR

/**
 * Reusable component for notes list item.
 *
 * Describes data rendered in [com.example.iteneraryapplication.R.layout.shared_list_note_item]
 * */
data class NoteItemViewDto(
    private val itemTitle: TextLine = TextLine.EMPTY,
    private val itemDateSaved: TextLine = TextLine.EMPTY,
    private val itemSubtitle: TextLine = TextLine.EMPTY,
    private val itemNote: TextLine = TextLine.EMPTY,
    val itemId: String,
    val itemNoteImage: String? = null,
    val itemNoteWebLink: String? = null,
    val itemNoteColor: String? = NOTES_DEFAULT_COLOR,
) {

    fun getItemTitle(context: Context) = itemTitle.getString(context)

    fun getItemDateSaved(context: Context) = itemDateSaved.getString(context)

    fun getSubtitle(context: Context) = itemSubtitle.getString(context)

    fun getItemNote(context: Context) = itemNote.getString(context)

    fun getNoteWebLinkHost() = itemNoteWebLink?.replace(DEFAULT_HTTPS_URL, "") ?: ""

    fun getDefaultURL() = DEFAULT_HTTPS_URL
}