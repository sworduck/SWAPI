package com.example.swapi.domain

import com.example.swapi.data.cache.BaseCacheDataSource
import com.example.swapi.utilis.Type
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteUseCase(private val characterCacheDataSource:BaseCacheDataSource) {

    fun onClickFavoriteButton(type: Type, id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val character = characterCacheDataSource.getCharacter(id)
            when(type){
                Type.FAVORITE->{
                    character.type = Type.DEFAULT
                }
                Type.DEFAULT->{
                    character.type = Type.FAVORITE
                }
            }
            characterCacheDataSource.updateCharacter(character)
        }
    }
}