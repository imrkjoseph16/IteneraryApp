package com.example.iteneraryapplication.register.domain

import com.example.iteneraryapplication.register.data.repository.RegisterRepository
import com.example.iteneraryapplication.app.shared.model.UserDetails
import javax.inject.Inject

class RegisterCredentialUseCase @Inject constructor(
    private val registerRepository: RegisterRepository
) {
    suspend fun registerCredentials(userDetails: UserDetails) = registerRepository.registerCredentials(userDetails)

    suspend fun sendEmailVerification() = registerRepository.sendEmailVerification()

    suspend fun saveFireStoreDetails(details: HashMap<String, String?>) = registerRepository.saveFireStoreDetails(details)
}