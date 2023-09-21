package com.example.iteneraryapplication.register.domain

import com.example.iteneraryapplication.register.data.form.RegisterForm
import com.example.iteneraryapplication.register.data.repository.RegisterRepository
import com.example.iteneraryapplication.shared.Credentials
import javax.inject.Inject

class CreateUserCredentials @Inject constructor(
    private val registerRepository: RegisterRepository
) {
    suspend operator fun invoke(credentials: Credentials) = registerRepository.registerCredentials(credentials)
}