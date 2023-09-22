package com.example.iteneraryapplication.app.shared.model

data class Credentials(
    val email: String,
    val phoneNumber: String? = null,
    val password: String
)