package com.example.swapi.data

import com.example.swapi.data.cache.CharacterCacheDataSource
import com.example.swapi.data.cache.CharacterDb
import com.example.swapi.data.cloud.CharacterListFromCloud
import io.realm.Realm

interface SearchRepository {
    suspend fun fetchCharacterList(page: Int):List<CharacterData>
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

}