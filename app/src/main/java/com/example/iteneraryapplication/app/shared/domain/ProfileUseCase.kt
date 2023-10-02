package com.example.iteneraryapplication.app.shared.domain

import com.example.iteneraryapplication.app.shared.data.ProfileRepository
import com.example.iteneraryapplication.app.shared.model.UserDetails
import dagger.Reusable
import javax.inject.Inject

@Reusable
class ProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {

    fun getProfileDetails(details: (userDetails: UserDetails?) -> Unit) =
        profileRepository.getProfileDetails(details = details)
}