package com.example.swapi.api

import com.example.swapi.api.RetrofitBuilder.apiService

class ApiHelper(private val apiService: ApiService) {
    suspend fun getCharacterList(page:Int) = apiService.fetchCharacters(page)
}