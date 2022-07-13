package com.example.swapi.api

import com.example.swapi.data.cloud.CharacterCloudList
import kotlinx.coroutines.Deferred
import okhttp3.ResponseBody
import org.json.JSONArray
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("api/people")
    suspend fun fetchCharacters(@Query("page") id: Int): CharacterCloudList

    @GET("api/people")
    fun getTripCoord(@Query("id") id: Int): Deferred<JSONArray>

    @GET("api/people/{id}")
    suspend fun fetchCharacter(@Path("id") id: Int): ResponseBody

    @GET("api/films")
    suspend fun fetchFilmList():ResponseBody
}