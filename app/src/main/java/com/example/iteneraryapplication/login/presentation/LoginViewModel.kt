package com.example.iteneraryapplication.login.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.iteneraryapplication.app.util.coRunCatching
import com.example.iteneraryapplication.login.domain.LoginUserCredentials
import com.example.iteneraryapplication.app.shared.model.UserDetails
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUserCredentials: LoginUserCredentials,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _loginState = MutableLiveData<LoginState>(ShowLoginNoData)
    val loginState: LiveData<LoginState> get() = _loginState

    fun loginCredentials(userDetails: UserDetails) {
        viewModelScope.launch {
            _loginState.apply {
                value = ShowLoginLoading

                coRunCatching {
                    loginUserCredentials.invoke(userDetails)
                }.onSuccess {
                    value = ShowLoginSuccess(firebaseAuth.currentUser?.isEmailVerified)
                }.onFailure {
                    value = ShowLoginError(it)
                }

                value = ShowLoginDismissLoading
            }
        }
    }
}