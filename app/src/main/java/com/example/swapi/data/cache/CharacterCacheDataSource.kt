package com.example.swapi.data.cache

import com.example.swapi.data.CharacterData

interface CharacterCacheDataSource {
    fun checkDataFromDB(page: Int): List<CharacterDataBaseEntity>

    fun fetchDataFromDB(page: Int): List<CharacterDataBaseEntity>

    suspend fun saveData(characterDataList: List<CharacterData>, page:Int)

    /*
    class Base():CharacterCacheDataSource{
        private var count = 9
        override fun setCount(page:Int){
            count = if (page ==9) 1 else 9
        }

        override fun checkDataFromDB(page: Int): List<CharacterDb>? {
            val realm = Realm.getDefaultInstance()
            return realm.where(CharacterDb::class.java).between(
                "id", 0 + (page - 1) * 10, count + (page - 1) * 10
            ).equalTo("type", "default").findAll()

        }

        override fun fetchDataFromDB(page: Int): List<CharacterDb>? {
            val realm = Realm.getDefaultInstance()
            return realm.where(CharacterDb::class.java).between(
                "id", 0 + (page - 1) * 10, count + (page - 1) * 10
            ).findAll()
        }
        override fun saveData(characterDataList: List<CharacterData>, page:Int) {
            val realm = Realm.getDefaultInstance()
            realm.executeTransaction { r: Realm ->
                val list = r.where(CharacterDb::class.java)
                    .between("id",characterDataList[0].id
                        ,characterDataList[characterDataList.size-1].id)
                    .equalTo("type","favorite").findAll().map {
                        it.id
                    }
                if(list.isEmpty()){
                    for(i in 0..count) {
                        val characterDb =
                            r.createObject(
                                CharacterDb::class.java,
                                characterDataList[i].id
                            )
                        characterDb.name = characterDataList[i].name
                        characterDb.height = characterDataList[i].height
                        characterDb.mass = characterDataList[i].mass
                        characterDb.homeworld = characterDataList[i].homeworld
                        characterDb.type = characterDataList[i].type
                        characterDb.idList = characterDataList[i].filmIdList
                        r.insertOrUpdate(characterDb)
                    }
                }
                else{
                    for(i in 0..count) {
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
                            characterDb.idList = characterDataList[i].filmIdList
                            r.insertOrUpdate(characterDb)
                        }
                    }
                }
            }
        }
    }

     */
    class Base():CharacterCacheDataSource {
        override fun checkDataFromDB(page: Int): List<CharacterDataBaseEntity> {
            return listOf()
        }

        override fun fetchDataFromDB(page: Int): List<CharacterDataBaseEntity> {
            return listOf()
        }

        override suspend fun saveData(characterDataList: List<CharacterData>, page: Int) {
            TODO("Not yet implemented")
        }
    }

    class BaseRoom(db:CharacterRoomDataBase):CharacterCacheDataSource{
        private val characterDataBaseDao: CharacterDataBaseDao = db.characterDataBaseDao()
        override fun checkDataFromDB(page: Int): List<CharacterDataBaseEntity> {
            return characterDataBaseDao.checkDataFromDB("default",page-1)
        }

        override fun fetchDataFromDB(page: Int): List<CharacterDataBaseEntity> {
            return characterDataBaseDao.getCharacterList(page-1)
        }

        override suspend fun saveData(characterDataList: List<CharacterData>, page: Int) {
            characterDataBaseDao.insertList(characterDataList.map { characterData -> characterData.mapToCharacterDataBaseEntity() })
            //val list = characterDataBaseDao.getAllCharacter()
            //val size = list.size
        }

    }
}