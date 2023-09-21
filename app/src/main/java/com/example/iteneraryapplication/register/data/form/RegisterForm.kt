package com.example.iteneraryapplication.register.data.form


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterForm(
    @SerialName("email")
    var email: String? = null,

    @SerialName("phone_number")
    var phoneNumber: String? = null,

    @SerialName("password")
    var password: String? = null,
)
