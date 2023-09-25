package com.example.iteneraryapplication.dashboard.pages.tripplanning

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.iteneraryapplication.app.shared.state.AppUiState
import com.example.iteneraryapplication.app.shared.state.AppUiStateModel
import com.example.iteneraryapplication.app.shared.state.GetAppUiItems
import com.example.iteneraryapplication.app.shared.state.ShowAppUiDismissLoading
import com.example.iteneraryapplication.app.shared.state.ShowAppUiError
import com.example.iteneraryapplication.app.shared.state.ShowAppUiLoading
import com.example.iteneraryapplication.app.shared.state.ShowAppUiNoData
import com.example.iteneraryapplication.app.util.coRunCatching
import com.example.iteneraryapplication.dashboard.shared.domain.PlanningNoteUseCase
import com.example.iteneraryapplication.dashboard.shared.domain.data.Notes
import com.example.iteneraryapplication.dashboard.shared.presentation.DashboardState
import com.example.iteneraryapplication.dashboard.shared.presentation.GetNotesTypeData
import com.example.iteneraryapplication.dashboard.shared.presentation.TravelNoteFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TripPlanningViewModel @Inject constructor(
    private val travelNoteFactory: TravelNoteFactory,
    private val planningNoteUseCase: PlanningNoteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<AppUiState>(ShowAppUiNoData)

    val items = _uiState.asStateFlow()

    private val cacheListNotes = mutableListOf<Notes>()

    fun getNotes(notesType: String) {
        viewModelScope.launch {
            _uiState.apply {
                value = ShowAppUiLoading

                coRunCatching {
                    planningNoteUseCase.getNotes(notesType = notesType) { newState ->
                        getUiItems(state = newState)
                    }
                }.onFailure {
                    value = ShowAppUiError(it)
                }

                value = ShowAppUiDismissLoading
            }
        }
    }

    private fun getUiItems(state: DashboardState) {
        (state as GetNotesTypeData).listNotes?.toMutableList()?.let { cacheListNotes.addAll(it) }
        travelNoteFactory.createOverview(state = state).also { newUiItems ->
            _uiState.update {
                GetAppUiItems(
                    uiStateModel = AppUiStateModel(
                        items = newUiItems,
                        isEmptyData = newUiItems.isEmpty()
                    )
                )
            }
        }
    }

    fun searchNotes(searchKey: String) {
        viewModelScope.launch {
            getUiItems(
                state = GetNotesTypeData(
                    listNotes = cacheListNotes.filter {
                        it.notesTitle?.startsWith(searchKey, ignoreCase = true) == true
                    }
                )
            )
        }
    }
}