package com.example.iteneraryapplication.login.presentation

open class LoginState

object ShowLoginNoData : LoginState()

object ShowLoginLoading : LoginState()

object ShowLoginDismissLoading : LoginState()

data class ShowLoginSuccess(val isVerified: Boolean?) : LoginState()

data class ShowLoginError(val throwable: Throwable) : LoginState()