package com.example.swapi.data.cache



class CharacterListCacheMapper(private val mapper:CharacterCacheMapper) {
    fun map(characterDbList: List<CharacterDb>) = characterDbList.map{ characterDb ->
        characterDb.map(mapper)
    }
}