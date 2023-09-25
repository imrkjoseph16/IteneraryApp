package com.example.iteneraryapplication.dashboard.shared.domain

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
}