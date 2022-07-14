package com.example.swapi.data.cache

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDataBaseDao {
    @Query("SELECT * FROM CharacterDataBaseEntity")
    fun getAllCharacter(): List<CharacterDataBaseEntity>

    @Query("SELECT * FROM CharacterDataBaseEntity WHERE idOnPage=:id")
    fun getCharacter(id: Int): CharacterDataBaseEntity

    @Query("SELECT * FROM CharacterDataBaseEntity WHERE idOnPage BETWEEN :page*10 AND :page*10+9")
    fun getCharacterList(page: Int): List<CharacterDataBaseEntity>

    @Query("SELECT * FROM CharacterDataBaseEntity WHERE type LIKE :type AND idOnPage BETWEEN :page*10 AND :page*10+9")
    fun checkDataFromDB(type: String, page: Int): List<CharacterDataBaseEntity>

    @Query("SELECT * FROM CharacterDataBaseEntity WHERE type LIKE :type")
    fun getCharacterListByType(type: String): List<CharacterDataBaseEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(characterDataBaseEntity: CharacterDataBaseEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertList(characterDataBaseEntityList: List<CharacterDataBaseEntity>)

    @Update()
    fun update(characterDataBaseEntity: CharacterDataBaseEntity)

    @Query("DELETE FROM CharacterDataBaseEntity WHERE idOnPage = :id")
    fun delete(id: Int)

    @Query("DELETE FROM CharacterDataBaseEntity")
    fun deleteAll()

    @Query("DELETE FROM CharacterDataBaseEntity WHERE type = :type")
    suspend fun deleteType(type: String)
}