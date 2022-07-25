package com.example.swapi.data

interface SearchRepository {
    suspend fun fetchCharacterList(page: Int): List<CharacterData>
}