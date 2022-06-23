package com.example.swapi.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.swapi.api.ApiHelper
import com.example.swapi.repository.CharacterListFromCloud
import com.example.swapi.viewmodel.MainViewModel

class ViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(CharacterListFromCloud.Base(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}