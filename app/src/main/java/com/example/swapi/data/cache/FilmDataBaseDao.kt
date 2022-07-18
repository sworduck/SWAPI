package com.example.swapi.data.cache

import androidx.room.*

@Dao
interface FilmDataBaseDao {
    @Query("SELECT * FROM FilmDataBaseEntity")
    fun getAllFilm(): List<FilmDataBaseEntity>

    @Query("SELECT * FROM FilmDataBaseEntity WHERE idOnPage=:id")
    fun getFilm(id: Int): FilmDataBaseEntity

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(characterDataBaseEntity: FilmDataBaseEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertList(characterDataBaseEntityList: List<FilmDataBaseEntity>)

    @Query("DELETE FROM FilmDataBaseEntity WHERE idOnPage = :id")
    fun delete(id: Int)

    @Query("DELETE FROM FilmDataBaseEntity")
    fun deleteAll()
}