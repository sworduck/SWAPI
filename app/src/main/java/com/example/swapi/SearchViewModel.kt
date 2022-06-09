package com.example.swapi

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.swapi.repository.MainRepository
import com.example.swapi.utilis.Resource
import io.realm.Realm
import kotlinx.coroutines.Dispatchers

class SearchViewModel(private val mainRepository: MainRepository) : ViewModel() {
        fun getCharacterList(page: Int) = liveData(Dispatchers.IO) {
                emit(Resource.loading(data = null))
                try {
                        emit(Resource.success(data = mainRepository.getCharacterList(page)))
                } catch (exception: Exception) {
                        emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
                }
        }

        private fun checkDatabase(): Boolean {
                /*
                var hasData: Boolean= false
                val mRealm = Realm.getDefaultInstance()
                mRealm!!.executeTransaction { realm ->
                        val rows = realm.where(Data::class.java).findAll()
                        hasData = rows.size > 0
                }
                return hasData

                 */
                return true
        }

        private fun saveData() {
                /*
                val mRealm = Realm.getDefaultInstance()
                mRealm!!.executeTransactionAsync({ bgRealm ->
                        for (i in languageResponse.data!!.indices) {
                                val datum = bgRealm.createObject(Data::class.java)
                                datum.key=languageResponse.data!![i].key
                                datum.code=languageResponse.data!![i].code
                                datum.lang=languageResponse.data!![i].lang
                                bgRealm.insert(datum)
                        }
                },
                        {
                                Toast.makeText(mContext,"Data insert into Database", Toast.LENGTH_LONG).show()
                                Log.d("status---->>>>", "Success")
                        })

                { // Transaction failed and was automatically canceled.
                        Toast.makeText(mContext,"Failed to data insert into Database", Toast.LENGTH_LONG).show()
                        Log.d("status", "failed")
                }

                 */
        }


        fun fetchDataFromDB(){
                /*
                val mRealm = Realm.getDefaultInstance()
                val mRealmLiveData = mRealm.where(Data::class.java).findAllAsync().asLiveData()
                list = Transformations.map(mRealmLiveData) {
                                realmResult ->
                        mRealm.copyFromRealm(realmResult)

                }
                Log.d("status---->>>>", list.toString())

                 */
        }

        /*
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

         */
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