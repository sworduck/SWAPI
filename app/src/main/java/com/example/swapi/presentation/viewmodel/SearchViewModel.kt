package com.example.swapi.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.swapi.data.SearchRepository
import com.example.swapi.data.cache.CharacterCacheDataSource
import com.example.swapi.data.cloud.CharacterListFromCloud
import com.example.swapi.data.cloud.FilmCloudList
import com.example.swapi.domain.ClickFavoriteButton
import com.example.swapi.utilis.Resource
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val characterListFromCloud:CharacterListFromCloud,
    private val characterCacheDataSource:CharacterCacheDataSource) : ViewModel() {

    var page: MutableLiveData<Int> = MutableLiveData<Int>(0)
    val clickFavoriteButton = ClickFavoriteButton(characterCacheDataSource)

    fun getCharacterList(page: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = SearchRepository.BaseRoom(characterListFromCloud,characterCacheDataSource).fetchCharacterList(page)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun saveFilmList() {
        CoroutineScope(Job() + Dispatchers.IO).launch {
            val filmDbList = characterCacheDataSource.fetchFilmList()
            if (filmDbList.isEmpty()) {
                val typeCharacter = object : TypeToken<FilmCloudList>() {}.type
                val filmList: FilmCloudList = Gson().fromJson(characterListFromCloud.fetchFilmList().string(), typeCharacter)
                characterCacheDataSource.saveFilmList(filmList.results?.let { filmCloudList->
                    filmCloudList.mapIndexed { id, filmCloud ->
                        filmCloud.mapToFilmDataBaseEntity(id)
                    }
                }.orEmpty())
            }
        }
    }
}

