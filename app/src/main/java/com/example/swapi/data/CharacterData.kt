package com.example.swapi.data

import com.example.swapi.data.cache.CharacterDataBaseEntity

data class CharacterData(val _id:Int){
    constructor(_id:Int,_name:String,_height:String,_mass:String,_filmIdList:String,_homeworld:String):this(_id){
        id = _id
        name = _name
        height = _height
        mass = _mass
        homeworld = _homeworld
        filmIdList = _filmIdList
    }
    constructor(_id:Int,_name:String,_height:String,_mass:String,_filmIdList:String,_homeworld:String,_type:String):this(_id){
        id = _id
        name = _name
        height = _height
        mass = _mass
        homeworld = _homeworld
        type = _type
        filmIdList = _filmIdList
    }
    var id = 0
    var name = "0"
    var height = "0"
    var mass = "0"
    var filmIdList = ""
    var homeworld = "0"
    var type = "default"// default and favorite
    fun mapToCharacterDataBaseEntity():CharacterDataBaseEntity{
        return CharacterDataBaseEntity(id,name,height,mass,homeworld,type, filmIdList)
    }
}