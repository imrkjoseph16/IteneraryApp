package com.example.iteneraryapplication.register.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.iteneraryapplication.app.core.shared.SingleLiveEvent
import com.example.iteneraryapplication.register.data.form.RegisterForm
import com.example.iteneraryapplication.register.domain.CreateUserCredentials
import com.example.iteneraryapplication.shared.Credentials
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val createUserCredentials: CreateUserCredentials,

    ) : ViewModel() {

    val isRegisterSuccess = SingleLiveEvent<Boolean?>()

    val throwMessage = SingleLiveEvent<String?>()

    private val fireStore = Firebase.firestore

    private val currentUser = FirebaseAuth.getInstance().currentUser
    fun registerCredentials(credentials: Credentials) {
        val userMap = hashMapOf(
            "email" to credentials.email,
            "phoneNumber" to credentials.phoneNumber,
            "password" to credentials.password

        )

        viewModelScope.launch {
            createUserCredentials.invoke(credentials).onSuccess {
                currentUser?.sendEmailVerification()?.addOnCompleteListener(
                    OnCompleteListener {
                        if (it.isSuccessful) {
                            val userId = currentUser!!.uid
                            fireStore.collection("user").document(userId).set(userMap)
                            isRegisterSuccess.value = true
                            throwMessage.value = "We have sent an email with a confirmation link to your email address. Please allow 5-10 minutes for this message to arrive."
                        } else {
                            isRegisterSuccess.value = false
                        }
                    })

            }.onFailure { throwMessage.value = it.message

            }
        }


    }
}