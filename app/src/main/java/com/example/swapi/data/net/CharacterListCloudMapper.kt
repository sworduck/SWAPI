package com.example.swapi.data.net

import com.example.swapi.data.CharacterData

class CharacterListCloudMapper(private val characterCloudMapper: CharacterCloudMapper) {
    var id:Int = 0
    fun map(characterCloudList:List<CharacterCloud>):List<CharacterData> =
        characterCloudList.map { characterCloud ->
            characterCloud.map(characterCloudMapper,id++)
        }
    fun map1(characterCloudList:List<CharacterCloud>,page:Int):List<CharacterData> {
        if(id==9)id=0
        return characterCloudList.map {
            characterCloud ->
            characterCloud.map(characterCloudMapper,(id++)+page)
        }
    }
}