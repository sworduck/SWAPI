package com.example.swapi

import android.content.Context
import android.provider.Settings.Global.getString
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.example.swapi.data.CharacterData
import com.example.swapi.data.SearchRepository
import com.example.swapi.data.cache.CharacterCacheDataSource
import com.example.swapi.data.cache.CharacterDb
import com.example.swapi.data.cloud.CharacterCloud
import com.example.swapi.data.cloud.CharacterCloudList
import com.example.swapi.data.cloud.CharacterListFromCloud
import io.realm.Realm
import io.realm.RealmConfiguration
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertArrayEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

@RunWith(MockitoJUnitRunner::class)
class DataLayerTests {
    @Mock
    private lateinit var mockContext: Context

    lateinit var mockRealm: Realm

    @Test
    fun testRepositorySaveCharacterList()= runBlocking {
        val repository = SearchRepository.Base(TestCharacterListFromCloud(),TestCharacterCacheDataSource())
        var actual = repository.fetchCharacterList(1)
        val expected = listOf(CharacterData(0,"1","1","1","1","1","default")
            ,CharacterData(1,"1","1","1","1","1","default"))
        assertEquals(expected,actual)
        actual = repository.fetchCharacterList(1)
        assertEquals(expected,actual)
    }


    class TestCharacterListFromCloud:CharacterListFromCloud {
        override suspend fun getCharacterList(page: Int): CharacterCloudList {
            return CharacterCloudList(1,"1","1",listOf<CharacterCloud>(CharacterCloud("1","1","1","1","1","1","1","1","1",
                listOf("1"), listOf("1"), listOf("1"), listOf("1"),"1","1","1")
                ,CharacterCloud("1","1","1","1","1","1","1","1","1",
                    listOf("1"), listOf("1"), listOf("1"), listOf("1"),"1","1","1")))
        }
    }


    class TestCharacterCacheDataSource: CharacterCacheDataSource{
        private var list:MutableList<CharacterDb> = mutableListOf()
        override fun checkDataFromDB(page: Int): List<CharacterDb>? {
            //not used here
            return listOf()
        }

        override fun fetchDataFromDB(page: Int): List<CharacterDb>? {
            return list
        }

        override fun saveData(characterDataList: List<CharacterData>, page: Int) {
            list.clear()
            list.addAll(characterDataList.map { characterData ->
                CharacterDb(characterData.id,characterData.name,characterData.height,characterData.mass,
                characterData.filmIdList,characterData.homeworld)
            })
        }

        override fun setCount(page: Int) {
            //not used here
        }

    }






}