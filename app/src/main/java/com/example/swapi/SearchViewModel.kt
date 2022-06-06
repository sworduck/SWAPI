package com.example.swapi

import androidx.lifecycle.ViewModel
import io.realm.Realm

class SearchViewModel : ViewModel() {
    class getRealmProvider{
        fun provide():Realm = Realm.getDefaultInstance()
    }
}