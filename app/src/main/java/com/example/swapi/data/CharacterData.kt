package com.example.swapi.data

import com.example.swapi.data.cache.character.CharacterDataBaseEntity
import com.example.swapi.utilis.Type

data class CharacterData(
    val id: Int,
    val name: String,
    val height: String,
    val mass: String,
    val filmIdList: String,
    val homeWorld: String,
    var type: Type = Type.DEFAULT
) {

    fun mapToCharacterDataBaseEntity(): CharacterDataBaseEntity {
        return CharacterDataBaseEntity(id, name, height, mass, homeWorld, type, filmIdList)
    }
}