package com.example.swapi

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        /*
        val BASE_URL = "https://swapi.dev/api/"
        CoroutineScope(Job() + Dispatchers.IO).launch {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .build()
            val typeCharacter = object : TypeToken<FilmCloudList>() {}.type
            val service = retrofit.create(CharacterService::class.java)
            val filmList: FilmCloudList = Gson().fromJson(service.fetchFilmList().string(), typeCharacter)
            var a = 1+1
        }

         */
        var a = "https://swapi.dev/api/films/6/".replace("/","").substringAfterLast("films")
        println("ЭТО ТА СТРОКА $a")
        assertEquals(4, 2 + 2)
    }
}