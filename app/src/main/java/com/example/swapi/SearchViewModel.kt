package com.example.swapi

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.swapi.repository.MainRepository
import com.example.swapi.utilis.Resource
import io.realm.Realm
import io.realm.kotlin.executeTransactionAwait
import kotlinx.coroutines.*

class SearchViewModel(private val mainRepository: MainRepository) : ViewModel() {

        fun getCharacterList(page: Int) = liveData(Dispatchers.IO) {
                emit(Resource.loading(data = null))
                try {
                        //преобразовать в characterData в листе
                        var result = mainRepository.getCharacterList(page).results
                        emit(Resource.success(data = result!!.mapIndexed {
                                        index, characterCloud -> characterCloud.map(index)
                        }))
                         //НЕ ПРОВЕРЕНО ЭКСПЕРЕМЕНТАЛЬНО
                                /*
                                saveData(result!!.mapIndexed {
                                                index, characterCloud -> characterCloud.map(index)
                                },page)

                                 */
                } catch (exception: Exception) {
                        emit( Resource.error( data = null, message = exception.message?: "Error Occurred!" ))
                }
        }

        fun checkDatabase(page: Int): Boolean {
                val realm = Realm.getDefaultInstance()
                var check = true
                        val rows = realm.where(CharacterDb::class.java).between("id",
                                0+(page-1)*10,9+(page-1)*10).findAll()
                        check = rows.size>0
                /*
                var check = false
                realm!!.executeTransaction { realm ->
                        val rows = realm.where(CharacterDb::class.java).findAll()
                        check = rows.size>0
                }
                return check

                 */
                return check
        }

        fun saveData(characterDataList: List<CharacterData>, page:Int) {
                Log.i("TAG", "saveData start")
                val realm = Realm.getDefaultInstance()
                Log.i("TAG", "realm init")
                /*
                realm.executeTransactionAwait {r:Realm->
                        Log.i("TAG", "Db save start${0}")
                        val characterDb =
                                r.createObject(CharacterDb::class.java,characterDataList[0].id)
                        //characterDb.id = characterDataList[0].id
                        characterDb.name = characterDataList[0].name
                        characterDb.height = characterDataList[0].height
                        characterDb.mass = characterDataList[0].mass
                        characterDb.homeworld = characterDataList[0].homeworld
                        Log.i("TAG", "Db save finish${0}")
                        //Доступ к объектам Realm возможен только в том потоке, в котором они были созданы.
                        r.insert(characterDb)
                        Log.i("TAG", "Db save finish${0}")
                }

                 */
                        //incorrect thread
                //Async query cannot be created on current thread. Realm cannot
                // be automatically updated on a thread without a looper.
                //сейчас добавление происходит без ошибок
                Log.i("TAG", "transaction start")
                realm.executeTransactionAsync { r: Realm ->
                        val datafromdb = r.where(CharacterDb::class.java).findAll()
                        Log.i("TAG", "inside transaction start")
                        //проверить createObject
                        for(i in 0..9) {
                                val characterDb =
                                        r.createObject(
                                                CharacterDb::class.java,
                                                characterDataList[i].id+(page-1)*10
                                        )
                                //characterDb.id = characterDataList[0].id
                                characterDb.name = characterDataList[i].name
                                characterDb.height = characterDataList[i].height
                                characterDb.mass = characterDataList[i].mass
                                characterDb.homeworld = characterDataList[i].homeworld
                                Log.i("TAG", "Db save finish${0}")
                                //Доступ к объектам Realm возможен только в том потоке, в котором они были созданы.
                                r.insertOrUpdate(characterDb)
                                //val datafromdb = r.where(CharacterDb::class.java).findAll()
                                Log.i("TAG", "inside transaction finish")
                        }

                }
                Log.i("TAG", "transaction finish")


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


        fun fetchDataFromDB(page: Int): List<CharacterDb>? {
                val realm = Realm.getDefaultInstance()
                /*
                return realm.where(CharacterDb::class.java).between(
                        "id", 0 + (page - 1) * 10, 9 + (page - 1) * 10
                ).findAll()

                 */
                return realm.where(CharacterDb::class.java).between(
                        "id", 0+(page-1)*10, 9+(page-1)*10
                ).findAll()
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