package com.example.iteneraryapplication.register.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.iteneraryapplication.app.util.coRunCatching
import com.example.iteneraryapplication.register.domain.RegisterCredentialUseCase
import com.example.iteneraryapplication.register.domain.data.ICreateUserCredential
import com.example.iteneraryapplication.register.domain.data.ISaveDetailsFireStore
import com.example.iteneraryapplication.register.domain.data.ISendEmailVerification
import com.example.iteneraryapplication.app.shared.model.UserDetails
import com.example.iteneraryapplication.register.domain.data.IRegisterCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerCredentialUseCase: RegisterCredentialUseCase
) : ViewModel() {

    private val _registerState = MutableLiveData<RegisterState>(ShowRegisterNoData)
    val registerState: LiveData<RegisterState> get() = _registerState

    fun registerCredentials(userDetails: UserDetails) {
        viewModelScope.launch {
            updateUIState(state = ShowRegisterLoading)

            // This chainCall function execute a step by step network call.
            registerCredentialUseCase.apply {
                chainCall(
                    { registerCredentials(userDetails = userDetails) },
                    { sendEmailVerification() },
                    { saveFireStoreDetails(details = userDetails.transformCredentials()) }
                )
            }

            updateUIState(state = ShowRegisterDismissLoading)
        }
    }

    private suspend fun<T : IRegisterCredential> chainCall(vararg calls: (suspend () -> T)) {
        run chain@ {
            calls.onEachIndexed { _, codeToExecute ->
                coRunCatching {
                    codeToExecute.invoke()
                }.onSuccess { result ->
                    if (result.isNetworkSuccess()) handleChainCallback(result)
                }.onFailure {
                    updateUIState(state = ShowRegisterError(it))
                    return@chain
                }
            }
        }
    }

    private fun UserDetails.transformCredentials() = hashMapOf(
        "email" to email,
        "phoneNumber" to phoneNumber,
        "password" to password
    )

    private fun updateUIState(state: RegisterState) {
        _registerState.value = state
    }

    private fun handleChainCallback(result: Any) {
        when(result) {
            is ICreateUserCredential -> updateUIState(state = RegisterCredentialSuccess(result.authResult))
            is ISendEmailVerification -> updateUIState(state = EmailVerificationSuccess)
            is ISaveDetailsFireStore -> updateUIState(state = SaveFireStoreDetailsSuccess)
        }
    }
}