package com.example.swapi

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FavoriteCharactersViewModel : ViewModel() {
    private val favoriteList = MutableLiveData<MutableList<CharacterData>>(mutableListOf())

    fun select(list: MutableList<CharacterData>) {
        favoriteList.value = list
    }

    fun addElementFavoriteList(element:CharacterData){
        favoriteList.value!!.add(element)
        Log.i("TAG","${favoriteList.value!!.size}")
    }



    fun getSelected(): LiveData<MutableList<CharacterData>>? {
        return favoriteList
    }
}