package com.example.iteneraryapplication.register.data.api

import com.example.iteneraryapplication.app.util.Network.Companion.APPLICATION_JSON
import com.example.iteneraryapplication.register.data.dto.cities.PlacesResponse
import retrofit2.http.*

interface RegisterApi {

    @Headers(APPLICATION_JSON)
    @GET("v1/cities")
    suspend fun getCities(): PlacesResponse

    @Headers(APPLICATION_JSON)
    @GET("v1/regions")
    suspend fun getRegions(): PlacesResponse
}