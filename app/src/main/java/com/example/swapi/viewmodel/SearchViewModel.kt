package com.example.swapi.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.swapi.data.CharacterData
import com.example.swapi.data.CharacterDb
import com.example.swapi.repository.MainRepository
import com.example.swapi.utilis.Resource
import io.realm.Realm
import kotlinx.coroutines.Dispatchers


class SearchViewModel(private val mainRepository: MainRepository) : ViewModel() {

        fun getCharacterList(page: Int) = liveData(Dispatchers.IO) {
                emit(Resource.loading(data = null))
                try {
                        var result = mainRepository.getCharacterList(page).results
                        emit(Resource.success(data = result!!.mapIndexed {
                                        index, characterCloud -> characterCloud.map(index+(page-1)*10)
                        }))
                } catch (exception: Exception) {
                        emit( Resource.error( data = null, message = exception.message?: "Error Occurred!" ))
                }
        }

        fun checkDatabase(page: Int, size: Int): Boolean {
                val realm = Realm.getDefaultInstance()
                var check = true
                        val rows = realm.where(CharacterDb::class.java).between("id",
                                0+(page-1)*10,9+(page-1)*10).findAll()
                        var rows2 = realm.where(CharacterDb::class.java).findAll()
                        check = rows.size==size
                return check
        }
        fun checkDatabase(page: Int): Boolean {
                val realm = Realm.getDefaultInstance()
                var check = true
                val rows = realm.where(CharacterDb::class.java).between("id",
                        0+(page-1)*10,9+(page-1)*10).findAll()
                check = if(page == 9)
                        rows.size==2
                else
                        rows.size==10
                return check
        }

        fun saveData(characterDataList: List<CharacterData>, page:Int) {
                val realm = Realm.getDefaultInstance()
                realm.executeTransactionAsync { r: Realm ->
                        val list = r.where(CharacterDb::class.java)
                                .between("id",characterDataList[0].id
                                ,characterDataList[characterDataList.size-1].id)
                                .equalTo("type","favorite").findAll().map {
                                it.id
                        }
                        if(list.isEmpty()){
                                for(i in 0..9) {
                                        val characterDb =
                                                r.createObject(
                                                        CharacterDb::class.java,
                                                        characterDataList[i].id
                                                )
                                        //characterDb.id = characterDataList[0].id
                                        characterDb.name = characterDataList[i].name
                                        characterDb.height = characterDataList[i].height
                                        characterDb.mass = characterDataList[i].mass
                                        characterDb.homeworld = characterDataList[i].homeworld
                                        characterDb.type = characterDataList[i].type
                                        //Доступ к объектам Realm возможен только в том потоке, в котором они были созданы.
                                        r.insertOrUpdate(characterDb)
                                }
                        }
                        else{
                                for(i in 0..9) {
                                        if(i+(page - 1) * 10 !in list) {
                                                val characterDb =
                                                        r.createObject(
                                                                CharacterDb::class.java,
                                                                characterDataList[i].id
                                                        )
                                                characterDb.name = characterDataList[i].name
                                                characterDb.height = characterDataList[i].height
                                                characterDb.mass = characterDataList[i].mass
                                                characterDb.homeworld =
                                                        characterDataList[i].homeworld
                                                characterDb.type = characterDataList[i].type
                                                //Доступ к объектам Realm возможен только в том потоке, в котором они были созданы.
                                                r.insertOrUpdate(characterDb)
                                        }
                                }
                        }


                }
        }


        fun fetchDataFromDB(page: Int): List<CharacterDb>? {
                val realm = Realm.getDefaultInstance()
                return realm.where(CharacterDb::class.java).between(
                        "id", 0+(page-1)*10, 9+(page-1)*10
                ).findAll()
        }
}

