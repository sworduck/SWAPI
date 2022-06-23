package com.example.swapi.data

import com.example.swapi.repository.CharacterListFromCloud
import io.realm.Realm

class SearchRepository(private val characterListFromCloud: CharacterListFromCloud) {
    suspend fun fetchCharacterList(page:Int):List<CharacterData>
    {
        return if (checkDataFromDB(page)!!.isEmpty()) {
            var resultFromCloud = characterListFromCloud.getCharacterList(page).results
            saveData(
                resultFromCloud!!.mapIndexed { i, characterCloud -> characterCloud.map(i+(page-1)*10) },
                page
            )
            resultFromCloud!!.mapIndexed { i, characterCloud -> characterCloud.map(i+(page-1)*10) }
        } else {
            fetchDataFromDB(page)!!.map { characterDb -> characterDb.map() }
        }
    }
    private fun fetchAllDb():List<CharacterDb>?{
        val realm = Realm.getDefaultInstance()
        return realm.where(CharacterDb::class.java).findAll()
    }

    private fun checkDataFromDB(page: Int): List<CharacterDb>? {
        val realm = Realm.getDefaultInstance()
        return realm.where(CharacterDb::class.java).between(
            "id", 0+(page-1)*10, 9+(page-1)*10
        ).equalTo("type","default").findAll()

    }

    private fun fetchDataFromDB(page: Int): List<CharacterDb>? {
        val realm = Realm.getDefaultInstance()
        return realm.where(CharacterDb::class.java).between(
            "id", 0 + (page - 1) * 10, 9 + (page - 1) * 10
        ).findAll()
    }
    private fun saveData(characterDataList: List<CharacterData>, page:Int) {
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

}