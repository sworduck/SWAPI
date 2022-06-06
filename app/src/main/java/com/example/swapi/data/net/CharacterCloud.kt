package com.example.swapi.data.net

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
    private val films: List<String>?=null,
    @SerializedName("species")
    private val species: List<String>?=null,
    @SerializedName("vehicles")
    private val vehicles: List<String>?=null,
    @SerializedName("starships")
    private val starships: List<String>?=null,
    @SerializedName("created")
    private val created: String,
    @SerializedName("edited")
    private val edited: String,
    @SerializedName("url")
    private val url: String
){
    fun map(characterCloudMapper: CharacterCloudMapper, id:Int) = characterCloudMapper.map(id,name,height,mass,homeworld)
}