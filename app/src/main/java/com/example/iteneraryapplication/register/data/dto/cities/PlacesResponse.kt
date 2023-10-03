package com.example.iteneraryapplication.register.data.dto.cities

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class PlacesResponse @JsonCreator constructor(
    @JsonProperty("data")
    val data: List<Data>,

    @JsonProperty("pagination")
    val pagination: Pagination
)

data class Data @JsonCreator constructor(
    @JsonProperty("href")
    val href: String,

    @JsonProperty("id")
    val id: String,

    @JsonProperty("name")
    val name: String,

    @JsonProperty("province_code")
    val provinceCode: String? = null,

    @JsonProperty("region_code")
    val regionCode: String? = null
)

data class Pagination @JsonCreator constructor(
    @JsonProperty("lastPage")
    val lastPage: Int,

    @JsonProperty("page")
    val page: Int,

    @JsonProperty("perPage")
    val perPage: Int,

    @JsonProperty("total")
    val total: Int
)