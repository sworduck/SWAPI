package com.example.swapi.data

import com.example.swapi.data.cache.BaseCacheDataSource
import com.example.swapi.data.cloud.BaseCloudDataSource

class SearchRepositoryWithoutCache(
        private val characterListFromCloud: BaseCloudDataSource,
        private val characterListFromCache: BaseCacheDataSource,
    ) : SearchRepository {

        override suspend fun fetchCharacterList(page: Int): List<CharacterData> {

            val resultFromCloud = characterListFromCloud.getCharacterList(page).results

            return resultFromCloud.let {
                it?.mapIndexed { i, characterCloud -> characterCloud.map(i + (page - 1) * 10) }
                    ?: listOf()
            }
        }
}