package com.example.swapi

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
    constructor(_id:Int,_name:String,_height:String,_mass:String,_homeworld:String,_type:String):this(){
        id = _id
        name = _name
        height = _height
        mass = _mass
        homeworld = _homeworld
        type = _type
    }
    @PrimaryKey
    var id = 0

    var name = "0"
    var height = "0"
    var mass = "0"
    var homeworld = "0"
    var type = "default"//default and favorite
    fun map() = CharacterData(id,name,height,mass,homeworld,type)
}