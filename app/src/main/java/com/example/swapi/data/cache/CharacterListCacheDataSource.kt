package com.example.swapi.data.cache

import com.example.swapi.data.CharacterData

class CharacterListCacheDataSource(private val realmProvider:RealmProvider) {

    fun fetchBooks():List<CharacterDb>{
        realmProvider.provide().use { realm->
            val characterListDb = realm.where(CharacterDb::class.java).findAll()?: emptyList()
            return realm.copyFromRealm(characterListDb)
        }
    }

    fun saveBooks(characterList:List<CharacterData>) = realmProvider.provide().use { realm->
        realm.executeTransaction {
            characterList.forEach { characterData->
                val bookDb = it.createObject(CharacterDb::class.java,characterData.id)
                bookDb.name = characterData.name
            }
        }
    }


}