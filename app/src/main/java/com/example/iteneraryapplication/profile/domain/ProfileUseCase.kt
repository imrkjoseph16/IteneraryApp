package com.example.iteneraryapplication.profile.domain

import com.example.iteneraryapplication.profile.data.ProfileRepository
import dagger.Reusable
import javax.inject.Inject

@Reusable
class ProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {

    suspend fun getProfileDetails() = profileRepository.getProfileDetails()
}