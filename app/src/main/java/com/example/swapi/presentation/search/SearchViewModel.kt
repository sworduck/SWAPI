package com.example.swapi.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.swapi.data.BaseSearchRepository
import com.example.swapi.data.CharacterData
import com.example.swapi.data.cache.BaseCacheDataSource
import com.example.swapi.data.cloud.BaseCloudDataSource
import com.example.swapi.data.cloud.film.FilmCloudList
import com.example.swapi.domain.FavoriteUseCase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val characterListFromCloud: BaseCloudDataSource,
    private val characterListFromCache: BaseCacheDataSource,
    private val clickFavoriteButton: FavoriteUseCase,
) : ViewModel() {

    private val _characterDataList: MutableLiveData<List<CharacterData>> = MutableLiveData()
    val characterDataList: LiveData<List<CharacterData>> = _characterDataList

    private val _errorMessage: MutableLiveData<String> = MutableLiveData()
    val errorMessage: LiveData<String> = _errorMessage

    fun viewCreated() {
        getCharacterList()
        CoroutineScope(Dispatchers.IO).launch {
            val filmDbList = characterListFromCache.fetchFilmList()
            if (filmDbList.isEmpty()) {
                val typeCharacter = object : TypeToken<FilmCloudList>() {}.type
                val filmList: FilmCloudList =
                    Gson().fromJson(characterListFromCloud.fetchFilmList().string(), typeCharacter)
                characterListFromCache.saveFilmList(filmList.results?.let { filmCloudList ->
                    filmCloudList.mapIndexed { id, filmCloud ->
                        filmCloud.mapToFilmDataBaseEntity(id)
                    }
                }.orEmpty())
            }
        }
    }

    private fun getCharacterList() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                _characterDataList.postValue(BaseSearchRepository(characterListFromCloud,
                    characterListFromCache).fetchCharacterList())
            } catch (e: Exception) {
                when (e) {
                    is HttpException -> _errorMessage.postValue("Отсутсвует интернет, попробуйте еще раз")
                    is UnknownHostException -> _errorMessage.postValue("Отсутсвует интернет, попробуйте еще раз")
                    else -> _errorMessage.postValue("Отсутсвует интернет, попробуйте еще раз")
                }
            }
        }
    }

    fun retryClicked(visible: Boolean) {
        if (visible) {
            getCharacterList()
        }
    }

    fun onClickFavoriteButton(characterData: CharacterData) {
        clickFavoriteButton.onClickFavoriteButton(characterData.type,
            characterData.id)
    }
}

