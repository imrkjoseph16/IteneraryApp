package com.example.iteneraryapplication.dashboard.shared.presentation

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.iteneraryapplication.app.util.Default.Companion.SOMETHING_WENT_WRONG
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

    fun executeDeleteNoteState(
        willDeleteImage: Boolean = false,
        noteImageUrl: String? = null,
        notesType: String? = null,
        notes: Notes
    ) {
        if (willDeleteImage) {
            deleteNoteImage(imageUrl = noteImageUrl)
        } else {
            deleteNotes(notesType, notes)
        }
    }

    fun deleteNoteImage(imageUrl: String? = null) {
        viewModelScope.launch {
            _dashboardState.apply {
                coRunCatching {
                    planningNoteUseCase.deleteNoteImage(imageUrl)
                }.onSuccess {
                    value = ShowDeleteImageSuccess(it)
                }.onFailure {
                    value = ShowDashboardError(it)
                }
            }
        }
    }

    fun deleteNotes(notesType: String? = null, notes: Notes) {
        viewModelScope.launch {
            _dashboardState.apply {
                value = ShowDashboardLoading

                coRunCatching {
                    planningNoteUseCase.deleteNotes(notesType, notes)
                }.onSuccess { isDeleted ->
                    value = if (isDeleted) ShowDeleteNotesSuccess
                    else ShowDashboardError(Throwable(SOMETHING_WENT_WRONG))
                }.onFailure {
                    value = ShowDashboardError(it)
                }

                value = ShowDashboardDismissLoading
            }
        }
    }

    fun uploadNoteImage(notesType: String, imageUri: Uri) {
        viewModelScope.launch {
            _dashboardState.apply {
                value = ShowDashboardLoading

                coRunCatching {
                    planningNoteUseCase.saveNoteImage(notesType = notesType, imageUri = imageUri)
                }.onSuccess {
                    value = ShowSaveImageSuccess(it)
                }.onFailure {
                    value = ShowDashboardError(Throwable(SOMETHING_WENT_WRONG))
                }

                value = ShowDashboardDismissLoading
            }
        }
    }
}