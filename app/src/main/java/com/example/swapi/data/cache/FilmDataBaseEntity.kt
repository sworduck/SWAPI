package com.example.swapi.data.cache

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.swapi.data.CharacterData

@Entity
data class FilmDataBaseEntity(
    @ColumnInfo val idOnPage : Int,
    @ColumnInfo val title : String,
    @ColumnInfo val opening_crawl : String
){
    @PrimaryKey(autoGenerate = true) var id : Int = 0


}