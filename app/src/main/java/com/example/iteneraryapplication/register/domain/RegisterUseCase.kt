package com.example.iteneraryapplication.register.domain

import com.example.iteneraryapplication.register.data.repository.RegisterRepository
import com.example.iteneraryapplication.app.shared.model.UserDetails
import dagger.Reusable
import javax.inject.Inject

@Reusable
class RegisterUseCase @Inject constructor(
    private val registerRepository: RegisterRepository
) {
    suspend fun registerCredentials(userDetails: UserDetails) = registerRepository.registerCredentials(userDetails)

    suspend fun sendEmailVerification() = registerRepository.sendEmailVerification()

    suspend fun saveFireStoreDetails(details: HashMap<String, String?>) = registerRepository.saveFireStoreDetails(details)

    suspend fun getCities() = registerRepository.getCities()

    suspend fun getRegions() = registerRepository.getRegions()
}