package com.example.iteneraryapplication.dashboard.shared.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.iteneraryapplication.app.util.Default.Companion.NOTES_TYPE_TRIP_PLAN
import com.example.iteneraryapplication.app.util.coRunCatching
import com.example.iteneraryapplication.dashboard.shared.domain.PlanningNoteUseCase
import com.example.iteneraryapplication.dashboard.shared.domain.data.Notes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardSharedViewModel @Inject constructor(
    private val planningNoteUseCase: PlanningNoteUseCase
) : ViewModel() {

    private val _dashboardState = MutableStateFlow<DashboardState>(ShowDashboardNoData)

    val dashboardState = _dashboardState.asStateFlow()

    fun saveNotes(notesType: String, notes: Notes) {
        viewModelScope.launch {
            _dashboardState.apply {
                value = ShowDashboardLoading

                coRunCatching {
                    planningNoteUseCase.saveNotes(notesType, notes)
                }.onSuccess {
                    value = ShowSaveNoteSuccess
                }.onFailure {
                    value = ShowDashboardError(it)
                }

                value = ShowDashboardDismissLoading
            }
        }
    }

    fun getNotes() {
        viewModelScope.launch {
            _dashboardState.apply {
                coRunCatching {
                    planningNoteUseCase.getNotes(NOTES_TYPE_TRIP_PLAN)
                }.onSuccess {
                    value = ShowGetNoteSuccess(it)
                }.onFailure {
                    value = ShowDashboardError(it)
                }
            }
        }
    }
}