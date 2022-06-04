package com.example.swapi

import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.realm.Realm
import io.realm.RealmList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import java.lang.reflect.Type

class SearchViewModel : ViewModel() {
    private val BASE_URL = "https://swapi.dev/api/"

    private var pageViewModel = 0

    fun getListOfCharacters(page:Int):MutableList<String>{

        return mutableListOf<String>()
    }

    fun nextAndPreviousCharacterListPage(page:Int): CharacterListCloud? {
        pageViewModel = page-1
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .build()
        val service = retrofit.create(CharacterService::class.java)
        val gson = Gson()
        val typelist = object: TypeToken<CharacterListCloud>(){}.type
        val scope = CoroutineScope(Job() + Dispatchers.Main)
        var listCloud: CharacterListCloud? = null
        scope.launch {
            //кэшируем данные
            val realm:Realm = Realm.getDefaultInstance()
            var characterDbList:List<CharacterDb> = realm.where(CharacterDb::class.java).findAll()?: emptyList()
            if(characterDbList.isEmpty()){
                val booksCloudList = cloudDataSource.fetchBooks()//получение через интернет
                val books = booksCloudMapper.map(booksCloudList)//cloud to character
                cacheDataSource.saveBooks(books)                //сохранение в кэше(в бд)
                BooksData.Success(books)                        //передача дальше(ретерн список)
            }
            else{
                BooksData.Success(booksCacheMapper.map(booksCacheList))//если дб не пустой передача дальше(ретерн список)

            }



            listCloud =gson.fromJson(service.fetchCharacters(page).string(),typelist)
            val listOfCharacters:MutableList<String> = mutableListOf()
            for(i in listCloud!!.results!!.indices){
                listOfCharacters.add(i,listCloud!!.results!![i].name)
            }

            realm.executeTransaction{ r:Realm ->
                    val realmList:RealmList<CharacterDb> = RealmList()
                realmList?.addAll(characterCloudToCharacterDb(listCloud!!.results,pageViewModel))
                realm!!.insertOrUpdate(realmList)
            }



            val a   = realm.where(CharacterDb::class.java).findAll()
            val b = 1+1
        }
        //как-то подождать окончания выполнения корутины
        return listCloud
    }

    private fun CharacterCloudToCharacter(mutableListCharacterCloud: List<CharacterCloud>):MutableList<Character>{
        val characterList:MutableList<Character> = mutableListOf()
        for(i in mutableListCharacterCloud.indices){
            characterList.add(i,Character(i+pageViewModel*10,mutableListCharacterCloud[i].name,mutableListCharacterCloud[i].height,
                mutableListCharacterCloud[i].mass,mutableListCharacterCloud[i].homeworld))
        }
        return characterList
    }
    private fun CharacterDbToCharacter(mutableListCharacterCloud: List<CharacterDb>):MutableList<Character>{
        val characterList:MutableList<Character> = mutableListOf()
        for(i in mutableListCharacterCloud.indices){
            characterList.add(i,Character(i+pageViewModel*10,mutableListCharacterCloud[i].name,mutableListCharacterCloud[i].height,
                mutableListCharacterCloud[i].mass,mutableListCharacterCloud[i].homeworld))
        }
        return characterList
    }
    private fun characterCloudToCharacterDb(mutableListCharacterCloud: List<CharacterCloud>?,page:Int):MutableList<CharacterDb>{
        var mutableListCharacterDb:MutableList<CharacterDb> = mutableListOf()
        for(i in mutableListCharacterCloud!!.indices){
            mutableListCharacterDb.add(i,CharacterDb(i+pageViewModel*10,mutableListCharacterCloud[i].name,mutableListCharacterCloud[i].height,
            mutableListCharacterCloud[i].mass,mutableListCharacterCloud[i].homeworld))
        }
        return mutableListCharacterDb
    }

}