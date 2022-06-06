package com.example.swapi.data.api

class ApiHelper(private val characterService: CharacterService) {
    suspend fun getCharacters(page:Int) = characterService.fetchCharacters(page)
}