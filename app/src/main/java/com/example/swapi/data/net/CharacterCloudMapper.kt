package com.example.swapi.data.net

import com.example.swapi.data.CharacterData
import com.example.swapi.data.cache.CharacterDb

class CharacterCloudMapper {
    fun map(_id:Int,_name:String,_height:String,_mass:String,_homeworld:String):CharacterData =
        CharacterData(_id,_name,_height,_mass,_homeworld)
}