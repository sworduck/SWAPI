package com.example.swapi.repository

import com.example.swapi.api.ApiHelper
import com.example.swapi.data.CharacterCloudList

interface CharacterListFromCloud {
    suspend fun getCharacterList(page: Int): CharacterCloudList
    class Base(private val apiHelper: ApiHelper):CharacterListFromCloud {
        override suspend fun getCharacterList(page: Int) = apiHelper.getCharacterList(page)
    }
}