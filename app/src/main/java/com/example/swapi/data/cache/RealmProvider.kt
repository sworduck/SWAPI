package com.example.swapi.data.cache

import io.realm.Realm

class RealmProvider {
    fun provide(): Realm = Realm.getDefaultInstance()
}