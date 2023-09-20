package com.example.iteneraryapplication.login.data.repository

import com.example.iteneraryapplication.app.util.coRunCatching
import com.example.iteneraryapplication.login.presentation.LoginState
import com.example.iteneraryapplication.login.presentation.ShowLoginError
import com.example.iteneraryapplication.login.presentation.ShowLoginLoading
import com.example.iteneraryapplication.login.presentation.ShowLoginNoData
import com.example.iteneraryapplication.login.presentation.ShowLoginSuccess
import com.example.iteneraryapplication.shared.Credentials
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {
    private val _dataStream = MutableStateFlow<LoginState>(ShowLoginNoData)

    suspend fun loginCredentials(credentials: Credentials) = coRunCatching {
        _dataStream.value = ShowLoginLoading
        firebaseAuth.signInWithEmailAndPassword(
            credentials.email,
            credentials.password
        ).await()
    }.onSuccess {
        _dataStream.value = ShowLoginSuccess
    }.onFailure {exception ->
        _dataStream.value = ShowLoginError(exception)
    }
}