package com.example.swapi.data.cloud

import com.example.swapi.api.ApiService
import okhttp3.ResponseBody
import javax.inject.Inject

interface CharacterListFromCloud {

    suspend fun getCharacterList(page: Int): CharacterCloudList
    suspend fun fetchFilmList(): ResponseBody

    class Base @Inject constructor (private val service: ApiService) : CharacterListFromCloud {
        override suspend fun getCharacterList(page: Int) = service.fetchCharacters(page)
        override suspend fun fetchFilmList()  = service.fetchFilmList()
    }
}