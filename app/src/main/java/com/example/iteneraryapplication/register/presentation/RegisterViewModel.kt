package com.example.iteneraryapplication.register.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.iteneraryapplication.app.util.coRunCatching
import com.example.iteneraryapplication.register.domain.RegisterUseCase
import com.example.iteneraryapplication.register.domain.data.ICreateUserCredential
import com.example.iteneraryapplication.register.domain.data.ISaveDetailsFireStore
import com.example.iteneraryapplication.register.domain.data.ISendEmailVerification
import com.example.iteneraryapplication.app.shared.model.UserDetails
import com.example.iteneraryapplication.app.util.ViewUtil
import com.example.iteneraryapplication.register.data.dto.cities.Data
import com.example.iteneraryapplication.register.data.repository.RegisterTransformer
import com.example.iteneraryapplication.register.domain.data.IRegisterCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PlacesModel(
    val cacheCities: MutableList<Data>? = null,
    val cacheRegions: MutableList<Data>? = null,
)

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val registerTransformer: RegisterTransformer,
    private val viewUtil: ViewUtil
) : ViewModel() {

    private val _registerState = MutableLiveData<RegisterState>(ShowRegisterNoData)
    val registerState: LiveData<RegisterState> get() = _registerState

    private val _cachedPlaces = MutableStateFlow(PlacesModel())

    init {
        getCities()
        getRegions()
    }

    fun registerCredentials(userDetails: UserDetails) {
        viewModelScope.launch {
            updateUIState(state = ShowRegisterLoading)

            // This chainCall function execute a step by step network call.
            registerUseCase.apply {
                chainCall(
                    { registerCredentials(userDetails = userDetails) },
                    { sendEmailVerification() },
                    { saveFireStoreDetails(details = userDetails.transformCredentials()) }
                )
            }

            updateUIState(state = ShowRegisterDismissLoading)
        }
    }

    private fun getCities() {
        viewModelScope.launch {
            coRunCatching {
                registerUseCase.getCities()
            }.onSuccess { resultCities ->
                _registerState.value = GetCitiesSuccess(
                    listOfCities = registerTransformer.transformToPlaces(resultCities)
                ).also {
                    _cachedPlaces.update { places -> places.copy(
                        cacheCities = resultCities.data.toMutableList())
                    }
                }
            }.onFailure {
                _registerState.value = ShowRegisterError(throwable = it)
            }
        }
    }

    private fun getRegions() {
        viewModelScope.launch {
            coRunCatching {
                registerUseCase.getRegions()
            }.onSuccess { resultRegion ->
                _cachedPlaces.update { places -> places.copy(
                    cacheRegions = resultRegion.data.toMutableList())
                }
            }.onFailure {
                _registerState.value = ShowRegisterError(throwable = it)
            }
        }
    }

    fun searchCityByRegion(cityPosition: Int) {
        if (viewUtil.verifyIfNegativeNumber(cityPosition)) return

        _cachedPlaces.value.apply {
            val cityRegion = mutableListOf<String>().apply {
                add(cacheRegions?.find { region ->
                    cacheCities?.get(cityPosition)?.regionCode == region.id
                }?.name ?: "Regions")
            }
            _registerState.value = GetRegionSuccess(listOfRegion = cityRegion)
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