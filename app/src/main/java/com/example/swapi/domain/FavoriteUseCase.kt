package com.example.swapi.domain

import com.example.swapi.data.cache.BaseCacheDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class FavoriteUseCase(private val characterCacheDataSource:BaseCacheDataSource) {

    fun onClickFavoriteButton(type: String, id: Int) {
        CoroutineScope(Job() + Dispatchers.IO).launch {
            val character = characterCacheDataSource.getCharacter(id)
            if (type == "default")
                character.type = "favorite"
            else//type=="favorite"
                character.type = "default"
            characterCacheDataSource.updateCharacter(character)
        }
    }
}