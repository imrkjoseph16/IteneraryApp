package com.example.iteneraryapplication.login.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.iteneraryapplication.app.core.shared.SingleLiveEvent
import com.example.iteneraryapplication.login.domain.LoginUserCredentials
import com.example.iteneraryapplication.shared.Credentials
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUserCredentials: LoginUserCredentials
) : ViewModel() {

    val isLoginSuccess = SingleLiveEvent<Boolean?>()

    val throwMessage = SingleLiveEvent<String?>()

    private val currentUser = FirebaseAuth.getInstance().currentUser

    fun loginCredentials(credentials: Credentials) {
        viewModelScope.launch {
            loginUserCredentials.invoke(credentials).onSuccess {
                isLoginSuccess.value = FirebaseAuth.getInstance().currentUser!!.isEmailVerified

            }.onFailure {
                // handle failed state
                throwMessage.value = it.message
            }
        }
    }
}