package com.example.iteneraryapplication.app.local.data.gateway.repository

import com.example.iteneraryapplication.app.local.data.form.NoteForm
import com.example.iteneraryapplication.app.local.domain.RoomDatabaseService
import javax.inject.Inject

class NoteRepository @Inject constructor(
    roomDatabaseService: RoomDatabaseService
) {

    private val commandClient = roomDatabaseService.commandDao()

    fun getNoteList() = commandClient.getNoteList()

    fun saveTravelNotes(noteForm: NoteForm) = commandClient.saveTravelNotes(noteForm)
}