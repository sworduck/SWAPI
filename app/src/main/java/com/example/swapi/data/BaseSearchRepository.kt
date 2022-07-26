package com.example.swapi.data

import com.example.swapi.data.cache.BaseCacheDataSource
import com.example.swapi.data.cloud.BaseCloudDataSource

class BaseSearchRepository(
    private val characterListFromCloud: BaseCloudDataSource,
    private val characterListFromCache: BaseCacheDataSource
) : SearchRepository {
    override suspend fun fetchCharacterList(page: Int): List<CharacterData> {

        val characterList = characterListFromCache.checkDataFromDB(page)

        if (characterList.isEmpty()) {
            val resultFromCloud = characterListFromCloud.getCharacterList(page).results
            characterListFromCache.saveData(resultFromCloud.let {
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
            return characterListFromCache.fetchDataFromDB(page)
                .map { characterDb -> characterDb.mapToCharacterData() }
        }
    }
}