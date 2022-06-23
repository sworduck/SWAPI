package com.example.swapi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.swapi.repository.CharacterListFromCloud
import com.example.swapi.utilis.Resource
import kotlinx.coroutines.Dispatchers

class MainViewModel(private val mainRepository: CharacterListFromCloud) : ViewModel() {

    fun getUsers(page:Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.getCharacterList(page)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
}