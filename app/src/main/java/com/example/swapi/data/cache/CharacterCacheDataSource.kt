package com.example.swapi.data.cache

import com.example.swapi.api.RetrofitBuilder
import com.example.swapi.data.CharacterData
import com.example.swapi.data.cloud.FilmCloudList
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

interface CharacterCacheDataSource {
    fun checkDataFromDB(page: Int): List<CharacterDataBaseEntity>

    fun fetchDataFromDB(page: Int): List<CharacterDataBaseEntity>

    fun getCharacter(id:Int):CharacterDataBaseEntity

    fun getCharacterListByType(type:String):List<CharacterDataBaseEntity>

    fun updateCharacter(characterDataBaseEntity: CharacterDataBaseEntity)

    suspend fun saveData(characterDataList: List<CharacterData>, page: Int)

    fun fetchFilmList():List<FilmDataBaseEntity>

    suspend fun saveFilmList(filmDataBaseEntityList: List<FilmDataBaseEntity>)

    class BaseRoom(db: CharacterRoomDataBase) : CharacterCacheDataSource {

        private val characterDataBaseDao: CharacterDataBaseDao = db.characterDataBaseDao()
        private val filmDataBaseDao: FilmDataBaseDao = db.filmDataBaseDao()

        override fun checkDataFromDB(page: Int): List<CharacterDataBaseEntity> {
            return characterDataBaseDao.checkDataFromDB("default", page - 1)
        }

        override fun fetchDataFromDB(page: Int): List<CharacterDataBaseEntity> {
            return characterDataBaseDao.getCharacterList(page - 1)
        }

        override fun updateCharacter(characterDataBaseEntity: CharacterDataBaseEntity) {
            characterDataBaseDao.update(characterDataBaseEntity)
        }

        override fun getCharacter(id: Int): CharacterDataBaseEntity {
            return characterDataBaseDao.getCharacter(id)
        }

        override fun getCharacterListByType(type: String): List<CharacterDataBaseEntity> {
            return characterDataBaseDao.getCharacterListByType(type)
        }

        override suspend fun saveData(characterDataList: List<CharacterData>, page: Int) {
            characterDataBaseDao.insertList(characterDataList.map { characterData -> characterData.mapToCharacterDataBaseEntity() })
        }

        override fun fetchFilmList(): List<FilmDataBaseEntity> {
            return filmDataBaseDao.getAllFilm()
        }

        override suspend fun saveFilmList(filmDataBaseEntityList: List<FilmDataBaseEntity>) {
            filmDataBaseDao.insertList(filmDataBaseEntityList)
        }
    }
}