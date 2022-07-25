package com.example.swapi.presentation.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.swapi.data.cache.BaseCacheDataSource
import com.example.swapi.data.cache.character.CharacterDataBaseEntity
import com.example.swapi.domain.FavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val characterListFromCache: BaseCacheDataSource
):ViewModel() {

    private val _listCharacter: MutableLiveData<List<CharacterDataBaseEntity>> = MutableLiveData()
    val listCharacter:LiveData<List<CharacterDataBaseEntity>> = _listCharacter
    val clickFavoriteButton = FavoriteUseCase(characterListFromCache)

    fun getCharacterListByType() {
        CoroutineScope(Job() + Dispatchers.IO).launch {
            _listCharacter.postValue(characterListFromCache.getCharacterListByType("favorite"))
        }
    }
}