package com.example.swapi.data

import com.example.swapi.data.cache.BaseCacheDataSource
import com.example.swapi.data.cloud.BaseCloudDataSource

class BaseSearchRepository(
    private val characterListFromCloud: BaseCloudDataSource,
    private val characterListFromCache: BaseCacheDataSource
) : SearchRepository {
    override suspend fun fetchCharacterList(): List<CharacterData> {

        val characterList = characterListFromCache.fetchDataFromDB()

        return if (characterList.isEmpty()) {
            val resultFromCloud = getCharacterListFromCloud()
            characterListFromCache.saveData(resultFromCloud)
            resultFromCloud
        } else {
            characterList
                .map { characterDb -> characterDb.mapToCharacterData() }
        }
    }

    private suspend fun getCharacterListFromCloud(): List<CharacterData> {
        var page = 1
        val result = arrayListOf<CharacterData>()
        do {
            val characterListFromCloud = characterListFromCloud.getCharacterList(page)
            result.addAll(characterListFromCloud.results.let {resultList-> resultList?.mapIndexed {
                    i,data-> data.map(i+ (page - 1) * 10) } } ?: listOf())
            page++
        } while (characterListFromCloud.next != null)
        return result.toList()
    }
}