package com.example.swapi.data

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class CharacterDb(): RealmObject(){
    constructor(_id:Int,_name:String,_height:String,_mass:String,_idList:String,_homeworld:String):this(){
        id = _id
        name = _name
        height = _height
        mass = _mass
        idList = _idList
        homeworld =_homeworld
    }
    constructor(_id:Int,_name:String,_height:String,_mass:String,_idList: String,_homeworld:String,_type:String):this(){
        id = _id
        name = _name
        height = _height
        mass = _mass
        idList = _idList
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
    var idList:String? = ""
    fun map() = CharacterData(id,name,height,mass,idList!!,homeworld,type)
}