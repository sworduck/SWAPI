package com.example.swapi.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.swapi.adapter.SearchFragmentAdapter
import com.example.swapi.data.cache.CharacterCacheDataSource
import com.example.swapi.data.cache.CharacterDataBaseEntity
import com.example.swapi.domain.ClickFavoriteButton
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val characterCacheDataSource: CharacterCacheDataSource
):ViewModel() {

    private val _listCharacter: MutableLiveData<List<CharacterDataBaseEntity>> = MutableLiveData()
    val listCharacter:LiveData<List<CharacterDataBaseEntity>> = _listCharacter
    val clickFavoriteButton = ClickFavoriteButton(characterCacheDataSource)

    fun getCharacterListByType() {
        CoroutineScope(Job() + Dispatchers.IO).launch {
            _listCharacter.postValue(characterCacheDataSource.getCharacterListByType("favorite"))
        }
    }
}