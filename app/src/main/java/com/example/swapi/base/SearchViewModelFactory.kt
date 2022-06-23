package com.example.swapi.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.swapi.viewmodel.SearchViewModel
import com.example.swapi.api.ApiHelper
import com.example.swapi.repository.CharacterListFromCloud

class SearchViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(CharacterListFromCloud.Base(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}