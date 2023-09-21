package com.example.iteneraryapplication.shared

data class Credentials(
    val email: String,
    val phoneNumber: String? = null,
    val password: String
)