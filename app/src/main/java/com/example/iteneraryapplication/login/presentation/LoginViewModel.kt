package com.example.iteneraryapplication.login.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.iteneraryapplication.app.core.shared.SingleLiveEvent
import com.example.iteneraryapplication.login.domain.LoginUserCredentials
import com.example.iteneraryapplication.shared.Credentials
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUserCredentials: LoginUserCredentials
) : ViewModel() {

    val isLoginSuccess = SingleLiveEvent<Boolean?>()

    fun loginCredentials(credentials: Credentials) {
        viewModelScope.launch {
            loginUserCredentials.invoke(credentials).onSuccess {
                // handle success state
                isLoginSuccess.value = true
            }.onFailure {
                // handle failed state
                isLoginSuccess.value = false
            }
        }
    }
}