package com.example.swapi.data.cache

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CharacterDataBaseEntity::class, FilmDataBaseEntity::class],
    version = 2,
    exportSchema = false)
abstract class CharacterRoomDataBase : RoomDatabase() {

    abstract fun characterDataBaseDao(): CharacterDataBaseDao
    abstract fun filmDataBaseDao(): FilmDataBaseDao

    companion object {
        @Volatile
        private var INSTANCE: CharacterRoomDataBase? = null

        fun getDataBase(context: Context): CharacterRoomDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CharacterRoomDataBase::class.java,
                    "room_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                //return instance
                instance
            }
        }
    }
}