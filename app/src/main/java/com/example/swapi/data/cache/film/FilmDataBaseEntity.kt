package com.example.swapi.data.cache.film

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.swapi.data.CharacterData
import com.example.swapi.data.FilmData

@Entity
data class FilmDataBaseEntity(
    @ColumnInfo val idOnPage: Int,
    @ColumnInfo val title: String,
    @ColumnInfo val opening_crawl: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    fun mapToFilmData(): FilmData {
        return FilmData(idOnPage, title, opening_crawl)
    }
}