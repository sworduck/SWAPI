package com.example.swapi.data.cloud

import com.example.swapi.data.CharacterData
import com.google.gson.annotations.SerializedName

data class CharacterCloud(
    @SerializedName("name")
    val name: String,
    @SerializedName("height")
    val height: String,
    @SerializedName("mass")
    val mass: String,
    @SerializedName("hair_color")
    private val hair_color: String,
    @SerializedName("skin_color")
    private val skin_color: String,
    @SerializedName("eye_color")
    private val eye_color: String,
    @SerializedName("birth_year")
    private val birth_year: String,
    @SerializedName("gender")
    private val gender: String,
    @SerializedName("homeworld")
    val homeworld: String,
    @SerializedName("films")
    val films: List<String> = listOf(),
    @SerializedName("species")
    private val species: List<String> = listOf(),
    @SerializedName("vehicles")
    private val vehicles: List<String> = listOf(),
    @SerializedName("starships")
    private val starships: List<String> = listOf(),
    @SerializedName("created")
    private val created: String,
    @SerializedName("edited")
    private val edited: String,
    @SerializedName("url")
    private val url: String) {

    fun map(id: Int) = CharacterData(id, name, height, mass,
        films.joinToString(), homeworld)
}