package com.example.swapi.data

import androidx.room.ColumnInfo

data class FilmData(
    val idOnPage: Int,
    val title: String,
    val opening_crawl: String
)