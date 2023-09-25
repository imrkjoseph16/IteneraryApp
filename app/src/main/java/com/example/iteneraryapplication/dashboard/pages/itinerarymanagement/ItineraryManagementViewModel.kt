package com.example.iteneraryapplication.dashboard.pages.itinerarymanagement

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
import com.example.iteneraryapplication.dashboard.shared.presentation.TravelNoteFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItineraryManagementViewModel @Inject constructor(
    private val travelNoteFactory: TravelNoteFactory,
    private val planningNoteUseCase: PlanningNoteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<AppUiState>(ShowAppUiNoData)

    val items = _uiState.asStateFlow()

    fun getNotes(notesType: String) {
        viewModelScope.launch {
            _uiState.apply {
                value = ShowAppUiLoading

                coRunCatching {
                    planningNoteUseCase.getNotes(notesType = notesType) { state ->
                        travelNoteFactory.createOverview(state = state).also { newUiItems ->
                            update {
                                GetAppUiItems(
                                    uiStateModel = AppUiStateModel(
                                        items = newUiItems,
                                        isEmptyData = newUiItems.isEmpty()
                                    )
                                )
                            }
                        }
                    }
                }.onFailure {
                    value = ShowAppUiError(it)
                }

                value = ShowAppUiDismissLoading
            }
        }
    }
}