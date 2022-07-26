package com.example.swapi.data.cloud

import javax.inject.Inject

class BaseCloudDataSource @Inject constructor(private val service: ApiService) : CloudDataSource {
    override suspend fun getCharacterList(page: Int) = service.fetchCharacters(page)
    override suspend fun fetchFilmList() = service.fetchFilmList()
}