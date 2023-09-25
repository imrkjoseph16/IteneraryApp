package com.example.iteneraryapplication.dashboard.shared.presentation

import com.example.iteneraryapplication.dashboard.shared.domain.data.Notes

open class DashboardState

object ShowDashboardNoData : DashboardState()

object ShowDashboardLoading : DashboardState()

object ShowDashboardDismissLoading : DashboardState()

object ShowDashboardSuccess : DashboardState()

object ShowSaveNoteSuccess : DashboardState()

data class ShowGetNoteSuccess(val notes: List<Notes>?) : DashboardState()

data class ShowDashboardError(val throwable: Throwable) : DashboardState()