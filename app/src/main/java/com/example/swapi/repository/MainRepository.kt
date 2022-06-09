package com.example.swapi.repository

import com.example.swapi.api.ApiHelper

class MainRepository(private val apiHelper: ApiHelper) {
    suspend fun getCharacterList(page:Int) = apiHelper.getCharacterList(page)
}