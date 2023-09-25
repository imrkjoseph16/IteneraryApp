package com.example.iteneraryapplication.dashboard.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.iteneraryapplication.app.shared.model.Credentials
import com.example.iteneraryapplication.app.util.coRunCatching
import com.example.iteneraryapplication.login.domain.LoginUserCredentials
import com.example.iteneraryapplication.login.presentation.LoginState
import com.example.iteneraryapplication.login.presentation.ShowLoginDismissLoading
import com.example.iteneraryapplication.login.presentation.ShowLoginError
import com.example.iteneraryapplication.login.presentation.ShowLoginLoading
import com.example.iteneraryapplication.login.presentation.ShowLoginSuccess
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

open class LogoutState

object ShowLogoutNoData : LogoutState()

object ShowLogoutLoading : LogoutState()

object ShowLogoutDismissLoading : LogoutState()

object ShowLogoutSuccess : LogoutState()

data class ShowLogoutError(val throwable: Throwable) : LogoutState()