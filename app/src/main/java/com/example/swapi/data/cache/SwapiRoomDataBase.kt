package com.example.swapi.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.swapi.data.cache.character.CharacterDataBaseDao
import com.example.swapi.data.cache.character.CharacterDataBaseEntity
import com.example.swapi.data.cache.film.FilmDataBaseDao
import com.example.swapi.data.cache.film.FilmDataBaseEntity

@Database(entities = [CharacterDataBaseEntity::class, FilmDataBaseEntity::class],
    version = 2,
    exportSchema = false)
abstract class SwapiRoomDataBase : RoomDatabase() {

    companion object {
        const val DATABASE_NAME = "notes_db"
    }

    abstract fun characterDataBaseDao(): CharacterDataBaseDao
    abstract fun filmDataBaseDao(): FilmDataBaseDao
}