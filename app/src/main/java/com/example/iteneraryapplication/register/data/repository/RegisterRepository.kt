package com.example.iteneraryapplication.register.data.repository

import com.example.iteneraryapplication.register.data.dto.RegisterCredentialResponse
import com.example.iteneraryapplication.register.data.dto.SaveDetailsFireStore
import com.example.iteneraryapplication.register.data.dto.SendEmailVerificationResponse
import com.example.iteneraryapplication.register.domain.data.ICreateUserCredential
import com.example.iteneraryapplication.register.domain.data.ISaveDetailsFireStore
import com.example.iteneraryapplication.register.domain.data.ISendEmailVerification
import com.example.iteneraryapplication.app.shared.model.UserDetails
import com.example.iteneraryapplication.register.data.api.RegisterApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Lazy
import kotlinx.coroutines.tasks.await
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RegisterRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val fireStore: FirebaseFirestore,
    private val retrofit: Lazy<Retrofit>
) {

    private val registerApi: RegisterApi by lazy { retrofit.get().create(RegisterApi::class.java) }

    suspend fun registerCredentials(userDetails: UserDetails) : ICreateUserCredential {
        val authResult = firebaseAuth.createUserWithEmailAndPassword(
            userDetails.email.orEmpty(),
            userDetails.password.orEmpty()
        ).await()
        return RegisterCredentialResponse(authResult)
    }

    suspend fun sendEmailVerification() : ISendEmailVerification {
        val emailVerificationTask = firebaseAuth.currentUser?.sendEmailVerification()
        emailVerificationTask?.await()
        return SendEmailVerificationResponse(emailVerificationTask?.isSuccessful == true)
    }

    suspend fun saveFireStoreDetails(details: HashMap<String, String?>) : ISaveDetailsFireStore {
        val userId = firebaseAuth.currentUser?.uid ?: error("userId failed to generate")
        val saveDetailsStatus = fireStore.collection("user").document(userId).set(details)
        saveDetailsStatus.await()
        return SaveDetailsFireStore(saveDetailsStatus.isSuccessful)
    }

    suspend fun getCities() = registerApi.getCities()

    suspend fun getRegions() = registerApi.getRegions()
}