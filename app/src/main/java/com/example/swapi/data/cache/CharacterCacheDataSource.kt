package com.example.swapi.data.cache

import com.example.swapi.data.CharacterData

interface CharacterCacheDataSource {
    fun checkDataFromDB(page: Int): List<CharacterDataBaseEntity>

    fun fetchDataFromDB(page: Int): List<CharacterDataBaseEntity>

    suspend fun saveData(characterDataList: List<CharacterData>, page:Int)

    class Base():CharacterCacheDataSource {
        override fun checkDataFromDB(page: Int): List<CharacterDataBaseEntity> {
            return listOf()
        }

        override fun fetchDataFromDB(page: Int): List<CharacterDataBaseEntity> {
            return listOf()
        }

        override suspend fun saveData(characterDataList: List<CharacterData>, page: Int) {
            TODO("Not yet implemented")
        }
    }

    class BaseRoom(db:CharacterRoomDataBase):CharacterCacheDataSource{
        private val characterDataBaseDao: CharacterDataBaseDao = db.characterDataBaseDao()
        override fun checkDataFromDB(page: Int): List<CharacterDataBaseEntity> {
            return characterDataBaseDao.checkDataFromDB("default",page-1)
        }

        override fun fetchDataFromDB(page: Int): List<CharacterDataBaseEntity> {
            return characterDataBaseDao.getCharacterList(page-1)
        }

        override suspend fun saveData(characterDataList: List<CharacterData>, page: Int) {
            characterDataBaseDao.insertList(characterDataList.map { characterData -> characterData.mapToCharacterDataBaseEntity() })
            //val list = characterDataBaseDao.getAllCharacter()
            //val size = list.size
        }

    }
}