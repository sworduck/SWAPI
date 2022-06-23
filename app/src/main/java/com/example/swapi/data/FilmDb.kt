package com.example.swapi.data

import io.realm.RealmObject
import io.realm.annotations.Ignore
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required

open class FilmDb():RealmObject() {
    constructor(_id:Int,_title:String,_opening_crawl:String):this(){
        id = _id
        title = _title
        opening_crawl = _opening_crawl
    }
    @PrimaryKey
    var id:Int = 0
    @Required
    var title:String? = ""
    @Required
    var opening_crawl:String? = ""
}