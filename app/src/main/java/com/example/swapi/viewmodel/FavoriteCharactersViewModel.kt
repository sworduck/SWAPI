package com.example.swapi.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.swapi.data.CharacterData

class FavoriteCharactersViewModel : ViewModel() {
    private val favoriteList = MutableLiveData<MutableList<CharacterData>>(mutableListOf())

    fun select(list: MutableList<CharacterData>) {
        favoriteList.value = list
    }

    fun addElementFavoriteList(element: CharacterData){
        favoriteList.value!!.add(element)
    }



    fun getSelected(): LiveData<MutableList<CharacterData>>? {
        return favoriteList
    }
}