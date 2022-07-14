package com.example.swapi.data.cloud

import androidx.room.ColumnInfo
import com.example.swapi.data.cache.FilmDataBaseEntity
import com.google.gson.annotations.SerializedName

data class FilmCloud(
    @SerializedName("title")
    val title: String,
    @SerializedName("episode_id")
    val episode_id: String,
    @SerializedName("opening_crawl")
    val opening_crawl: String,
    @SerializedName("director")
    val director: String,
    @SerializedName("producer")
    val producer: String,
    @SerializedName("release_date")
    val release_date: String,
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
    val url: String,
) {
    fun mapToFilmDataBaseEntity(id: Int): FilmDataBaseEntity {
        return FilmDataBaseEntity(id, title, opening_crawl)
    }
}