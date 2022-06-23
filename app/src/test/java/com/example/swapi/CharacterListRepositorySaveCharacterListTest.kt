package com.example.swapi

import com.example.swapi.data.CharacterCloud
import com.example.swapi.data.CharacterCloudList
import com.example.swapi.data.CharacterData
import com.example.swapi.data.SearchRepository
import com.example.swapi.repository.CharacterListFromCloud
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class CharacterListRepositorySaveCharacterListTest {
    @Test
    fun testSaveCharacterList() = runBlocking {
        var characterListFromCloud = CharacterListFromCloudTest()

        var actual = SearchRepository(characterListFromCloud).fetchCharacterList(1)
        var expected = listOf(CharacterCloud("name1","height1","mass1","hair1","skin1",
            "eye1","birth1","gender1","homeworld1",null,null,
            null,null,"created1","edited1","url1"),
            CharacterCloud("name2","height2","mass2","hair2","skin2",
                "eye2","birth2","gender2","homeworld2",null,null,
                null,null,"created2","edited2","url2 ")
        )
        assertEquals(expected,actual)


        //var characterDataList = repository.getCharacterList(1)

    }

    class CharacterListFromCloudTest:CharacterListFromCloud {
        override suspend fun getCharacterList(page: Int): CharacterCloudList {
            return  CharacterCloudList(10,"next",null,
                listOf(CharacterCloud("name1","height1","mass1","hair1","skin1",
                "eye1","birth1","gender1","homeworld1",null,null,
                    null,null,"created1","edited1","url1"),
                       CharacterCloud("name2","height2","mass2","hair2","skin2",
                           "eye2","birth2","gender2","homeworld2",null,null,
                           null,null,"created2","edited2","url2 ")
                ))
        }
    }


}