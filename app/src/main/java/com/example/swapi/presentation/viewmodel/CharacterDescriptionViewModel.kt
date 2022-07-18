package com.example.swapi.presentation.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.swapi.R
import com.example.swapi.data.FilmData
import com.example.swapi.data.SearchRepository
import com.example.swapi.data.cache.CharacterCacheDataSource
import com.example.swapi.data.cache.FilmDataBaseEntity
import com.example.swapi.domain.ClickFavoriteButton
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterDescriptionViewModel @Inject constructor(
    private val characterCacheDataSource: CharacterCacheDataSource
):ViewModel() {

    private val _buttonStateLiveData = MutableLiveData(R.drawable.ic_baseline_star_rate_24)
    val buttonStateLiveData: LiveData<Int> = _buttonStateLiveData

    private val _filmListLiveData = MutableLiveData<List<FilmData>>()
    val filmListLiveData: LiveData<List<FilmData>> = _filmListLiveData

    private val _characterDescription = MutableLiveData<String>()
    val characterDescription: LiveData<String> = _characterDescription


    fun viewCreated(position: Int){
        CoroutineScope(Job() + Dispatchers.IO).launch{
            val character = characterCacheDataSource.getCharacter(position)
            val list: List<Int> = character.idList.split(",").map {
                it.replace("/", "").substringAfterLast("films").toInt()
            }
            val filmListDb = characterCacheDataSource.fetchFilmList()
            val filmsDb:MutableList<FilmDataBaseEntity> = mutableListOf()

            list.forEach {
                filmsDb.add(filmListDb[it-1])
            }

            _characterDescription.postValue("Name: ${character.name} \nMass: ${character.mass}" +
                    "\nHeight: ${character.height}")

            _filmListLiveData.postValue(filmsDb.map { filmDataBaseEntity -> filmDataBaseEntity.mapToFilmData() })
        }
    }

    fun buttonClicked(position: Int){
        CoroutineScope(Job() + Dispatchers.IO).launch{
            val character = characterCacheDataSource.getCharacter(position)

            if (character.type == "default")
                character.type = "favorite"
            else//type=="favorite"
                character.type = "default"

            characterCacheDataSource.updateCharacter(character)

            if(character.type == "default") _buttonStateLiveData.postValue(R.drawable.ic_baseline_star_border_24)
            else _buttonStateLiveData.postValue(R.drawable.ic_baseline_star_rate_24)
        }
    }
}