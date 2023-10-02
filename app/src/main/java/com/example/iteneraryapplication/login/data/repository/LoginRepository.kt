package com.example.iteneraryapplication.login.data.repository

import com.example.iteneraryapplication.app.shared.model.UserDetails
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginRepository @Inject constructor(private val firebaseAuth: FirebaseAuth) {
    suspend fun loginCredentials(userDetails: UserDetails): AuthResult = firebaseAuth.signInWithEmailAndPassword(
        userDetails.email.orEmpty(),
        userDetails.password.orEmpty()
    ).await()
}