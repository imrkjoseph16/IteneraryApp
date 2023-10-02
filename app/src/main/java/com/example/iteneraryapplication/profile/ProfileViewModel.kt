package com.example.iteneraryapplication.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.iteneraryapplication.app.shared.domain.ProfileUseCase
import com.example.iteneraryapplication.app.shared.model.UserDetails
import com.example.iteneraryapplication.app.util.coRunCatching
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileUseCase: ProfileUseCase
) : ViewModel() {

    private val _profileDetails = MutableLiveData<UserDetails>()

    val profileDetails: LiveData<UserDetails> get() = _profileDetails

    fun getProfileDetails() {
        viewModelScope.launch {
            coRunCatching {
                profileUseCase.getProfileDetails { details ->
                    _profileDetails.value = details
                }
            }
        }
    }
}