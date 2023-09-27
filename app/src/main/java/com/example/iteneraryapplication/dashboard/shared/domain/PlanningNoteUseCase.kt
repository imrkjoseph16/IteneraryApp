package com.example.iteneraryapplication.dashboard.shared.domain

import android.net.Uri
import com.example.iteneraryapplication.dashboard.shared.data.DashboardRepository
import com.example.iteneraryapplication.dashboard.shared.domain.data.Notes
import com.example.iteneraryapplication.dashboard.shared.presentation.DashboardState
import dagger.Reusable
import javax.inject.Inject

@Reusable
class PlanningNoteUseCase @Inject constructor(
    private val dashboardRepository: DashboardRepository
) {

    fun getNotes(notesType: String, getNotesItem: (appUiState: DashboardState) -> Unit) = dashboardRepository.getNotes(notesType = notesType, getNotesItem = getNotesItem)

    suspend fun saveNotes(notesType: String, notes: Notes) = dashboardRepository.saveNotes(notesType = notesType, notes = notes)

    suspend fun deleteNotes(notesType: String? = null, notes: Notes) = dashboardRepository.deleteNotes(notesType = notesType, notes = notes)

    suspend fun saveNoteImage(notesType: String, imageUri: Uri) = dashboardRepository.saveNoteImage(
        notesType = notesType, imageUri = imageUri
    )

    suspend fun deleteNoteImage(imageUrl: String?) = dashboardRepository.deleteNoteImage(imageUrl)
}