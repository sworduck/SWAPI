package com.example.swapi.data.cloud.film

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class FilmCloudList(
    @SerializedName("count")
    val count: Int,
    @SerializedName("next")
    val next: String?,
    @SerializedName("previous")
    val previous: String?,
    @SerializedName("results")
    val results: List<FilmCloud>? = null
)