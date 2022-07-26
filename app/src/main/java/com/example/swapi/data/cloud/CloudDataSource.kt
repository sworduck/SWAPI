package com.example.swapi.data.cloud

import com.example.swapi.data.cloud.character.CharacterCloudList
import okhttp3.ResponseBody

interface CloudDataSource {
    suspend fun getCharacterList(page: Int): CharacterCloudList
    suspend fun fetchFilmList(): ResponseBody
}