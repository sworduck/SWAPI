package com.example.swapi.data.cloud.character

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable


@Serializable
data class CharacterCloudList(
    @SerializedName("count")
    val count: Int,
    @SerializedName("next")
    val next: String?,
    @SerializedName("previous")
    val previous: String?,
    @SerializedName("results")
    val results: List<CharacterCloud>? = null,
)