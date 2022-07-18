package com.example.swapi.data.cache

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CharacterDataBaseEntity::class, FilmDataBaseEntity::class],
    version = 2,
    exportSchema = false)
abstract class CharacterRoomDataBase : RoomDatabase() {

    companion object {
        const val DATABASE_NAME = "notes_db"
    }

    abstract fun characterDataBaseDao(): CharacterDataBaseDao
    abstract fun filmDataBaseDao(): FilmDataBaseDao
}