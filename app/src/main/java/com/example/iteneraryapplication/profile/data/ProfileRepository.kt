package com.example.iteneraryapplication.profile.data

import com.example.iteneraryapplication.app.shared.model.UserDetails
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepository @Inject constructor(
    private val fireStore: FirebaseFirestore,
    firebaseUser: FirebaseUser?
) {

    private val userId = firebaseUser?.uid ?: error("failed to fetch userId")

    suspend fun getProfileDetails() : UserDetails? {
        val fireStorePath = fireStore.collection("user").document(userId).get()
        fireStorePath.await()

        if (fireStorePath.isSuccessful) return fireStorePath.result.toObject(UserDetails::class.java)
        else error("failed to fetch details")
    }
}