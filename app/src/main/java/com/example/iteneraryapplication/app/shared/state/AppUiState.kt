package com.example.iteneraryapplication.app.shared.state

open class AppUiState

object ShowAppUiNoData : AppUiState()

object ShowAppUiLoading : AppUiState()

object ShowAppUiDismissLoading : AppUiState()

data class GetAppUiItems(val uiStateModel: AppUiStateModel) : AppUiState()

data class ShowAppUiError(val throwable: Throwable) : AppUiState()

data class AppUiStateModel(
    val items: List<Any> = emptyList(),
    val isEmptyData: Boolean = false,
    val error: Throwable? = null
)