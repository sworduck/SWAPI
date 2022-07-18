package com.example.swapi.data

import com.example.swapi.data.cache.CharacterDataBaseEntity

data class CharacterData(val id: Int = 0,
                         val name:String = "0",
                         val height:String = "0",
                         val mass:String = "0",
                         val filmIdList:String = "",
                         val homeworld:String = "0",
                         var type:String = "default") {

    fun mapToCharacterDataBaseEntity(): CharacterDataBaseEntity {
        return CharacterDataBaseEntity(id, name, height, mass, homeworld, type, filmIdList)
    }
}