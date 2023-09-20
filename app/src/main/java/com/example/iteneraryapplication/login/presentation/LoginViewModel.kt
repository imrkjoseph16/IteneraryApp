package com.example.iteneraryapplication.login.presentation

import androidx.lifecycle.ViewModel
import com.example.iteneraryapplication.login.domain.LoginUserCredentials
import com.example.iteneraryapplication.shared.Credentials
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUserCredentials: LoginUserCredentials
) : ViewModel() {

    suspend fun loginCredentials(credentials: Credentials) {
        loginUserCredentials.invoke(credentials).onSuccess {
            // handle success state
        }.onFailure {
            // handle failed state
        }
    }
}