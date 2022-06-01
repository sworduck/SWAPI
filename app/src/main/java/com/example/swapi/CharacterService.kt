package com.example.swapi

import kotlinx.coroutines.Deferred
import okhttp3.ResponseBody
import org.json.JSONArray
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CharacterService {
    @GET("people")
    suspend fun fetchCharacters(@Query("page") id: Int): ResponseBody

    @GET("people")
    fun getTripCoord(@Query("id") id: Int): Deferred<JSONArray>

    @GET("people/{id}")
    suspend fun fetchCharacter(@Path("id")id: Int): ResponseBody
}