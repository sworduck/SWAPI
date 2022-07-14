package com.example.swapi.data

import androidx.annotation.WorkerThread
import com.example.swapi.data.cache.CharacterCacheDataSource
import com.example.swapi.data.cache.CharacterDataBaseDao
import com.example.swapi.data.cache.CharacterDataBaseEntity
import com.example.swapi.data.cloud.CharacterListFromCloud

interface SearchRepository {
    suspend fun fetchCharacterList(page: Int): List<CharacterData>
    class BaseRoom(     //without cache
        private val characterListFromCloud: CharacterListFromCloud,
        private val characterCacheDataSource: CharacterCacheDataSource,
    ) : SearchRepository {

        override suspend fun fetchCharacterList(page: Int): List<CharacterData> {

            val resultFromCloud = characterListFromCloud.getCharacterList(page).results

            return resultFromCloud.let {
                it?.mapIndexed { i, characterCloud -> characterCloud.map(i + (page - 1) * 10) }
                    ?: listOf()
            }
        }
    }

    class BaseRoomTwo(
        private val characterListFromCloud: CharacterListFromCloud,
        private val characterCacheDataSource: CharacterCacheDataSource,
    ) : SearchRepository {
        override suspend fun fetchCharacterList(page: Int): List<CharacterData> {

            val characterList = characterCacheDataSource.checkDataFromDB(page)

            if (characterList.isEmpty()) {
                val resultFromCloud = characterListFromCloud.getCharacterList(page).results
                characterCacheDataSource.saveData(resultFromCloud.let {
                    it?.mapIndexed { i, characterCloud ->
                        characterCloud.map(i + (page - 1) * 10)
                    } ?: listOf()
                }, page)
                return resultFromCloud.let {
                    it?.mapIndexed { i, characterCloud ->
                        characterCloud.map(i + (page - 1) * 10)
                    } ?: listOf()
                }
            } else {
                return characterCacheDataSource.fetchDataFromDB(page)
                    .map { characterDb -> characterDb.mapToCharacterData() }
            }

        }
    }

}