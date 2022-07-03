package com.example.swapi.data

import com.example.swapi.api.ApiHelper

interface CharacterListFromCloud {
    suspend fun getCharacterList(page: Int): CharacterCloudList
    class Base(private val apiHelper: ApiHelper): CharacterListFromCloud {
        override suspend fun getCharacterList(page: Int) = apiHelper.getCharacterList(page)
    }
}