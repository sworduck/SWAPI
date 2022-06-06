package com.example.swapi.data.cache

import com.example.swapi.data.CharacterData
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey


open class CharacterDb(): RealmObject(){
    constructor(_id:Int,_name:String,_height:String,_mass:String,_homeworld:String):this(){
        id = _id
        name = _name
        height = _height
        mass = _mass
        homeworld = _homeworld
    }
    @PrimaryKey
    var id = 0

    var name = "0"
    var height = "0"
    var mass = "0"
    var homeworld = "0"
    fun map(mapper: CharacterCacheMapper) = CharacterData(id,name,height,mass,homeworld)
}