package com.example.swapi.data.cache

import com.example.swapi.data.CharacterData
import com.example.swapi.data.cache.character.CharacterDataBaseDao
import com.example.swapi.data.cache.character.CharacterDataBaseEntity
import com.example.swapi.data.cache.film.FilmDataBaseDao
import com.example.swapi.data.cache.film.FilmDataBaseEntity
import com.example.swapi.utilis.Type

class BaseCacheDataSource(db: SwapiRoomDataBase) : CacheDataSource {

    private val characterDataBaseDao: CharacterDataBaseDao = db.characterDataBaseDao()
    private val filmDataBaseDao: FilmDataBaseDao = db.filmDataBaseDao()

    override fun checkDataFromDB(page: Int): List<CharacterDataBaseEntity> {
        return characterDataBaseDao.checkDataFromDB(Type.DEFAULT, page - 1)
    }

    override fun fetchDataFromDB(page: Int): List<CharacterDataBaseEntity> {
        return characterDataBaseDao.getCharacterList(page - 1)
    }

    override fun updateCharacter(characterDataBaseEntity: CharacterDataBaseEntity) {
        characterDataBaseDao.update(characterDataBaseEntity)
    }

    override fun getCharacter(id: Int): CharacterDataBaseEntity {
        return characterDataBaseDao.getCharacter(id)
    }

    override fun getCharacterListByType(type: Type): List<CharacterDataBaseEntity> {
        return characterDataBaseDao.getCharacterListByType(type)
    }

    override suspend fun saveData(characterDataList: List<CharacterData>, page: Int) {
        characterDataBaseDao.insertList(characterDataList.map { characterData -> characterData.mapToCharacterDataBaseEntity() })
    }

    override fun fetchFilmList(): List<FilmDataBaseEntity> {
        return filmDataBaseDao.getAllFilm()
    }

    override suspend fun saveFilmList(filmDataBaseEntityList: List<FilmDataBaseEntity>) {
        filmDataBaseDao.insertList(filmDataBaseEntityList)
    }
}