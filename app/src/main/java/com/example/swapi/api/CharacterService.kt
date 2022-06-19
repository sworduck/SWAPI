package com.example.swapi.api

import kotlinx.coroutines.Deferred
import okhttp3.ResponseBody
import org.json.JSONArray
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CharacterService {
    @GET("api/people")
    suspend fun fetchCharacters(@Query("page") id: Int): ResponseBody

    @GET("api/people")
    fun getTripCoord(@Query("id") id: Int): Deferred<JSONArray>

    @GET("api/people/{id}")
    suspend fun fetchCharacter(@Path("id") id: Int): ResponseBody

    @GET("api/films/{id}")
    suspend fun fetchFilm(@Path("id") id: Int): ResponseBody

    @GET("api/vehicles/{id}")
    suspend fun fetchVehicles(@Path("id") id: Int): ResponseBody

    @GET("api/vehicles/{id}")
    suspend fun fetchStarship(@Path("id") id: Int): ResponseBody
}