package com.example.swapi.data.net

import com.example.swapi.data.api.CharacterService

class CharacterListCloudDataSource(private val service: CharacterService) {
    suspend fun fetchCharacterList(page:Int) = service.fetchCharacters(page)
}