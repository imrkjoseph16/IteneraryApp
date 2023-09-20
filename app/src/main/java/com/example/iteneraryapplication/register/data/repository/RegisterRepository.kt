package com.example.iteneraryapplication.register.data.repository

import com.example.iteneraryapplication.app.util.coRunCatching
import com.example.iteneraryapplication.register.domain.data.Credentials
import com.example.iteneraryapplication.register.presentation.RegisterState
import com.example.iteneraryapplication.register.presentation.ShowRegisterError
import com.example.iteneraryapplication.register.presentation.ShowRegisterLoading
import com.example.iteneraryapplication.register.presentation.ShowRegisterNoData
import com.example.iteneraryapplication.register.presentation.ShowRegisterSuccess
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RegisterRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {
    private val _dataStream = MutableStateFlow<RegisterState>(ShowRegisterNoData)

    suspend fun registerCredentials(credentials: Credentials) = coRunCatching {
        _dataStream.value = ShowRegisterLoading
        firebaseAuth.signInWithEmailAndPassword(
            credentials.email,
            credentials.password
        ).await()
    }.onSuccess {
        _dataStream.value = ShowRegisterSuccess
    }.onFailure {exception ->
        _dataStream.value = ShowRegisterError(exception)
    }
}