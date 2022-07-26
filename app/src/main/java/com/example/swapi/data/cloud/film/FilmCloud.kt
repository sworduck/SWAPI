package com.example.swapi.data.cloud.film

import com.example.swapi.data.cache.film.FilmDataBaseEntity
import com.google.gson.annotations.SerializedName

data class FilmCloud(
    @SerializedName("title")
    val title: String,
    @SerializedName("episode_id")
    val episodeId: String,
    @SerializedName("opening_crawl")
    val openingCrawl: String,
    @SerializedName("director")
    val director: String,
    @SerializedName("producer")
    val producer: String,
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("characters")
    val characters: List<String>? = null,
    @SerializedName("planets")
    val planets: List<String>? = null,
    @SerializedName("starships")
    val starships: List<String>? = null,
    @SerializedName("vehicles")
    val vehicles: List<String>? = null,
    @SerializedName("species")
    val species: List<String>? = null,
    @SerializedName("created")
    val created: String,
    @SerializedName("edited")
    val edited: String,
    @SerializedName("url")
    val url: String
) {
    fun mapToFilmDataBaseEntity(id: Int): FilmDataBaseEntity {
        return FilmDataBaseEntity(id, title, openingCrawl)
    }
}