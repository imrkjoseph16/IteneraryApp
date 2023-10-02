package com.example.iteneraryapplication.dashboard.shared.presentation

import com.example.iteneraryapplication.dashboard.shared.domain.data.Notes

open class DashboardState

object ShowDashboardNoData : DashboardState()

object ShowDashboardLoading : DashboardState()

object ShowDashboardDismissLoading : DashboardState()

object ShowSaveNoteSuccess : DashboardState()

object ShowDeleteNotesSuccess : DashboardState()

data class GetNotesTypeData(val listNotes: List<Notes>?) : DashboardState()

data class ShowSaveImageSuccess(val imageUrl: String) : DashboardState()

data class ShowDeleteImageSuccess(val isDeleteSuccess: Boolean) : DashboardState()

data class ShowDashboardError(val throwable: Throwable) : DashboardState()