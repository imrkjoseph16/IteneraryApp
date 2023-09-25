package com.example.iteneraryapplication.dashboard.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.iteneraryapplication.app.util.coRunCatching
import com.example.iteneraryapplication.dashboard.shared.presentation.LogoutState
import com.example.iteneraryapplication.dashboard.shared.presentation.ShowLogoutDismissLoading
import com.example.iteneraryapplication.dashboard.shared.presentation.ShowLogoutError
import com.example.iteneraryapplication.dashboard.shared.presentation.ShowLogoutLoading
import com.example.iteneraryapplication.dashboard.shared.presentation.ShowLogoutSuccess
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _logoutState = MutableLiveData<LogoutState>()
    val logoutState: LiveData<LogoutState> get() = _logoutState

    fun logout() {
        viewModelScope.launch {
            _logoutState.apply {
                value = ShowLogoutLoading
                coRunCatching {
                    firebaseAuth.signOut()
                }.onSuccess {
                    value = ShowLogoutSuccess
                }.onFailure {
                    value = ShowLogoutError(it)
                }
                value = ShowLogoutDismissLoading
            }
        }
    }
}