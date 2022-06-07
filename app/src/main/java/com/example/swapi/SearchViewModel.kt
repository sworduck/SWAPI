package com.example.swapi

import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.realm.Realm
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import retrofit2.Retrofit

class SearchViewModel : ViewModel() {
        private val BASE_URL = "https://swapi.dev/api/"
        private fun provideRealm():Realm = Realm.getDefaultInstance()
        suspend fun fetchCharacterList(page:Int):List<CharacterData>{
                var id = 0
                val realm = provideRealm()
                val characterDbList = realm.where(CharacterDb::class.java).findAll()?: emptyList()
                if(characterDbList.isEmpty()){
                        val characterCloudList = fetchCharacterCloudList(page)
                        val characterDataList = characterCloudList!!.map { characterCloud ->
                                characterCloud.map((id++)+(page-1)*10)
                        }

                        realm.executeTransaction {
                                characterDataList.forEach { characterData->
                                        val CharacterDb = it.createObject(CharacterDb::class.java,characterData.id)
                                        CharacterDb.name = characterData.name
                                }
                        }
                        return characterDataList
                }
                else{
                    return characterDbList.map { characterDb ->
                            characterDb.map()
                    }
                }
        }
        suspend fun fetchCharacterCloudList(page: Int):List<CharacterCloud>?{
                val retrofit = Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .build()
                val service = retrofit.create(CharacterService::class.java)
                val gson = Gson()
                val typelist = object: TypeToken<CharacterCloudList>(){}.type
                val scope = CoroutineScope(Job() + Dispatchers.Main)
                var cloudList: CharacterCloudList? = null
                cloudList =gson.fromJson(service.fetchCharacters(page).string(),typelist)
                val listOfCharacters:MutableList<String> = mutableListOf()
                for(i in cloudList!!.results!!.indices){
                        listOfCharacters.add(i,cloudList!!.results!![i].name)
                }
                return cloudList.results
        }
}

/*
наговнокодить -
1)трай получаю данные с кэша
3)если данные в кэше получить данные из кэша
4)если данных в кэше нет
4.1 получить данные с интернета
4.2 перевести данные в обычные из интернетных
4.3 добавить данные в кэш
4.4 вернуть?? отправить по Success
*.* если ошибка вернуть по Fail
 */