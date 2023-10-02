package com.example.iteneraryapplication.app.shared.data

import com.example.iteneraryapplication.app.shared.model.UserDetails
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepository @Inject constructor(
    private val fireStore: FirebaseFirestore,
    firebaseUser: FirebaseUser?
) {

    private val userId = firebaseUser?.uid ?: error("failed to fetch userId")

    fun getProfileDetails(details: (details: UserDetails?) -> Unit) {
        val fireStorePath = fireStore.collection("user").document(userId).get()

        fireStorePath.addOnCompleteListener {
            if (it.isSuccessful) details.invoke(
                it.result.toObject(UserDetails::class.java)
            )
        }
    }
}