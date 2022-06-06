package com.example.swapi.data.cache

import com.example.swapi.data.CharacterData

class CharacterCacheMapper {
    fun map(characterDb: CharacterDb):CharacterData =CharacterData(
        characterDb.id,characterDb.name,
        characterDb.height,characterDb.mass,
        characterDb.homeworld)
}