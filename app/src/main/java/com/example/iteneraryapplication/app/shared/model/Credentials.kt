package com.example.iteneraryapplication.app.shared.model

data class Credentials(
    val firstName: String? = null,
    val lastName: String? = null,
    val suffix: String? = null,
    val gender: String? = null,
    val address: String? = null,
    val city: String? = null,
    val region: String? = null,
    val email: String,
    val phoneNumber: String? = null,
    val password: String
)