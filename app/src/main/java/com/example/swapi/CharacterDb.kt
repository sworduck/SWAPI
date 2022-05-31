package com.example.swapi

import android.os.Parcel
import android.os.Parcelable
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required


open class CharacterDb() : RealmObject() {
    constructor(_name:String, _birth_year:String, _eye_color:String, _gender:String, _hair_color:String
                , _height:String, _mass:String, _skin_color:String, _homeworld:String):this(){
        name = _name
        birth_year = _birth_year
        eye_color = _eye_color
        gender = _gender
        hair_color = _hair_color
        height = _height
        mass = _mass
        skin_color = _skin_color
        homeworld = _homeworld
    }

    @PrimaryKey
    var name: String = ""

    @Required
    var birth_year: String? = ""

    @Required
    var eye_color: String? = ""

    @Required
    var gender: String? = ""

    @Required
    var hair_color: String? = ""

    @Required
    var height: String? = ""

    @Required
    var mass: String? = ""

    @Required
    var skin_color: String? = ""

    @Required
    var homeworld: String? = ""
}