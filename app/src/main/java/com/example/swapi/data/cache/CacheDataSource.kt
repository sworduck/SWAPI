package com.example.swapi.data.cache

import com.example.swapi.data.CharacterData
import com.example.swapi.data.cache.character.CharacterDataBaseEntity
import com.example.swapi.data.cache.film.FilmDataBaseEntity
import com.example.swapi.utilis.Type

interface CacheDataSource {
    fun fetchDataFromDB(): List<CharacterDataBaseEntity>

    fun getCharacter(id: Int): CharacterDataBaseEntity

    fun getCharacterListByType(type: Type): List<CharacterDataBaseEntity>

    fun updateCharacter(characterDataBaseEntity: CharacterDataBaseEntity)

    suspend fun saveData(characterDataList: List<CharacterData>)

    fun fetchFilmList(): List<FilmDataBaseEntity>

    suspend fun saveFilmList(filmDataBaseEntityList: List<FilmDataBaseEntity>)
}