package com.example.swapi.data

interface SearchRepository {
    suspend fun fetchCharacterList(): List<CharacterData>
}