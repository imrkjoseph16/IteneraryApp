package com.example.iteneraryapplication.register.data.repository

import com.example.iteneraryapplication.register.data.dto.cities.PlacesResponse
import javax.inject.Inject

class RegisterTransformer @Inject constructor() {

    fun transformToPlaces(placesResponse: PlacesResponse) =
        placesResponse.data.map {
            it.name
        }.toMutableList()
}