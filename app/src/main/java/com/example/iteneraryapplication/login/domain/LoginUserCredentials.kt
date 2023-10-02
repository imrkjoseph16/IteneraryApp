package com.example.iteneraryapplication.login.domain

import com.example.iteneraryapplication.login.data.repository.LoginRepository
import com.example.iteneraryapplication.app.shared.model.UserDetails
import javax.inject.Inject
class LoginUserCredentials @Inject constructor(
    private val loginRepository: LoginRepository
) {
    suspend operator fun invoke(userDetails: UserDetails) = loginRepository.loginCredentials(userDetails)
}