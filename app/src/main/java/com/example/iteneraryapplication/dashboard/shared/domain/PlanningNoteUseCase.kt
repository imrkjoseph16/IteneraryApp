package com.example.iteneraryapplication.dashboard.shared.domain

import android.net.Uri
import com.example.iteneraryapplication.dashboard.shared.data.DashboardRepository
import com.example.iteneraryapplication.dashboard.shared.domain.data.Notes
import dagger.Reusable
import javax.inject.Inject

@Reusable
class PlanningNoteUseCase @Inject constructor(
    private val dashboardRepository: DashboardRepository
) {

    suspend fun saveNotes(notesType: String, notes: Notes) = dashboardRepository.saveNotes(notesType = notesType, notes = notes)

    fun getNotes(notesType: String) = dashboardRepository.getNotes(notesType = notesType)

    suspend fun saveNoteImage(notesType: String, imageUri: Uri) = dashboardRepository.saveNoteImage(
        notesType = notesType, imageUri = imageUri
    )
}