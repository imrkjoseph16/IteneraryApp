package com.example.iteneraryapplication.register.presentation

import androidx.lifecycle.ViewModel
import com.example.iteneraryapplication.register.domain.CreateUserCredentials
import com.example.iteneraryapplication.register.domain.data.Credentials
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val createUserCredentials: CreateUserCredentials
) : ViewModel() {

    suspend fun registerCredentials(credentials: Credentials) {
        createUserCredentials.invoke(credentials).onSuccess {
            // handle success state
        }.onFailure {
            // handle failed state
        }
    }
}