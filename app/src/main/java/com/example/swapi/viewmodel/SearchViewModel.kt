package com.example.swapi.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.swapi.api.ApiHelper
import com.example.swapi.api.RetrofitBuilder
import com.example.swapi.data.CharacterData
import com.example.swapi.data.CharacterDb
import com.example.swapi.data.SearchRepository
import com.example.swapi.data.CharacterListFromCloud
import com.example.swapi.utilis.Resource
import io.realm.Realm
import kotlinx.coroutines.Dispatchers


class SearchViewModel() : ViewModel() {
        private val mainRepository = CharacterListFromCloud.Base(ApiHelper(RetrofitBuilder.apiService))
        var page: MutableLiveData<Int> = MutableLiveData<Int>()
        fun getCharacterList(page: Int) = liveData(Dispatchers.IO) {
                emit(Resource.loading(data = null))
                try {
                        emit(Resource.success(data = SearchRepository(mainRepository).fetchCharacterList(page)))
                } catch (exception: Exception) {
                        emit(Resource.error( data = null, message = exception.message?: "Error Occurred!" ))
                }
        }
}

