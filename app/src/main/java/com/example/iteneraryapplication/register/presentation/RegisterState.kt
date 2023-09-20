package com.example.iteneraryapplication.register.presentation

open class RegisterState

object ShowRegisterNoData : RegisterState()

object ShowRegisterLoading : RegisterState()

object ShowRegisterDismissLoading : RegisterState()

object ShowRegisterSuccess : RegisterState()

data class ShowRegisterError(val throwable: Throwable) : RegisterState()