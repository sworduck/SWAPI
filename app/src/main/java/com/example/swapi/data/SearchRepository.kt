package com.example.swapi.data

import androidx.annotation.WorkerThread
import com.example.swapi.data.cache.CharacterCacheDataSource
import com.example.swapi.data.cache.CharacterDataBaseDao
import com.example.swapi.data.cache.CharacterDataBaseEntity
import com.example.swapi.data.cache.CharacterDb
import com.example.swapi.data.cloud.CharacterListFromCloud
import io.realm.Realm
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    suspend fun fetchCharacterList(page: Int):List<CharacterData>
    /*
    class Base(private val characterListFromCloud: CharacterListFromCloud,
    private val characterCacheDataSource: CharacterCacheDataSource):SearchRepository{

        override suspend fun fetchCharacterList(page:Int):List<CharacterData>
        {
            characterCacheDataSource.setCount(page)
            var characterList = characterCacheDataSource.checkDataFromDB(page)
            if (characterList!!.isEmpty()) {
                var resultFromCloud = characterListFromCloud.getCharacterList(page).results
                characterCacheDataSource.saveData(
                    resultFromCloud!!.mapIndexed { i, characterCloud -> characterCloud.map(i+(page-1)*10) },
                    page
                )
                return resultFromCloud!!.mapIndexed { i, characterCloud -> characterCloud.map(i+(page-1)*10) }
            } else {
                return characterCacheDataSource.fetchDataFromDB(page)!!.map { characterDb -> characterDb.map() }
            }
        }
    }

     */

    /*
    class BaseRoom(private val characterDataBaseDao:CharacterDataBaseDao):SearchRepository{
        val allCharacterDataBaseDao: Flow<List<CharacterDataBaseEntity>> = characterDataBaseDao.getCharacterList()
        override suspend fun fetchCharacterList(page: Int): List<CharacterData> {
            return listOf()
        }
        @Suppress("RedundantSuspendModifier")
        @WorkerThread
        suspend fun insert(characterDataBaseEntity: CharacterDataBaseEntity){
            characterDataBaseDao.insert(characterDataBaseEntity)
        }
    }

     */
    class BaseRoom(private val characterListFromCloud: CharacterListFromCloud,
                   private val characterCacheDataSource: CharacterCacheDataSource):SearchRepository{
        override suspend fun fetchCharacterList(page: Int): List<CharacterData> {
            var resultFromCloud = characterListFromCloud.getCharacterList(page).results
            return resultFromCloud!!.mapIndexed { i, characterCloud -> characterCloud.map(i+(page-1)*10) }
        }
    }
    class BaseRoomTwo(private val characterListFromCloud: CharacterListFromCloud,
                   private val characterCacheDataSource: CharacterCacheDataSource):SearchRepository{
        override suspend fun fetchCharacterList(page: Int): List<CharacterData> {

            var characterList = characterCacheDataSource.checkDataFromDB(page)
            if (characterList!!.isEmpty()) {
                var resultFromCloud = characterListFromCloud.getCharacterList(page).results
                characterCacheDataSource.saveData(
                    resultFromCloud!!.mapIndexed { i, characterCloud -> characterCloud.map(i+(page-1)*10) },
                    page
                )
                return resultFromCloud!!.mapIndexed { i, characterCloud -> characterCloud.map(i+(page-1)*10) }
            } else {
                return characterCacheDataSource.fetchDataFromDB(page)!!.map { characterDb -> characterDb.mapToCharacterData() }
            }

        }
    }

}