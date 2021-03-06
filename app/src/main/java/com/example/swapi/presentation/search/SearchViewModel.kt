package com.example.swapi.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.swapi.data.BaseSearchRepository
import com.example.swapi.data.cache.BaseCacheDataSource
import com.example.swapi.data.cloud.BaseCloudDataSource
import com.example.swapi.data.cloud.film.FilmCloudList
import com.example.swapi.domain.FavoriteUseCase
import com.example.swapi.utilis.ErrorType
import com.example.swapi.utilis.Resource
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val characterListFromCloud:BaseCloudDataSource,
    private val characterListFromCache:BaseCacheDataSource,
    val clickFavoriteButton: FavoriteUseCase) : ViewModel() {

    companion object{
        private const val LAST_PAGE: Int = 9
    }

    private val _characterDescription = MutableLiveData<String>()
    val characterDescription: LiveData<String> = _characterDescription

    private val _page: MutableLiveData<Int> = MutableLiveData<Int>(1)
    val page:LiveData<Int> = _page

    private val _previousPage: MutableLiveData<Int> = MutableLiveData<Int>(1)

    fun viewCreated(){
        saveFilmList()
    }

    fun getCharacterList() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
        emit(Resource.success(data = BaseSearchRepository(characterListFromCloud,characterListFromCache).fetchCharacterList(page.value ?: 1)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = mapError(exception)))
        }
    }



    private fun mapError(e:Exception) = when (e) {
            is HttpException -> ErrorType.SERVICE_UNAVAILABLE
            is UnknownHostException  -> ErrorType.NO_CONNECTION
            else -> ErrorType.GENERIC_ERROR
    }



    private fun saveFilmList() {
        CoroutineScope(Job() + Dispatchers.IO).launch {
            val filmDbList = characterListFromCache.fetchFilmList()
            if (filmDbList.isEmpty()) {
                val typeCharacter = object : TypeToken<FilmCloudList>() {}.type
                val filmList: FilmCloudList = Gson().fromJson(characterListFromCloud.fetchFilmList().string(), typeCharacter)
                characterListFromCache.saveFilmList(filmList.results?.let { filmCloudList->
                    filmCloudList.mapIndexed { id, filmCloud ->
                        filmCloud.mapToFilmDataBaseEntity(id)
                    }
                }.orEmpty())
            }
        }
    }

    fun retryClicked(visible: Boolean) {
        if(visible){
            _page.value = _previousPage.value
        }
    }

    fun nextClicked() {
        if (_page.value ?: 1 + 1 in 1..LAST_PAGE) {
            _previousPage.value = _page.value
            _page.value = (page.value)?.plus(1)
        }
    }

    fun previousClicked() {
        if (_page.value ?: 1 - 1 in 1..LAST_PAGE) {
            _previousPage.value = _page.value
            _page.value = (_page.value)?.minus(1)
        }
    }
}

