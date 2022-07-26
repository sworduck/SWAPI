package com.example.swapi.data.cache.character


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.swapi.data.CharacterData
import com.example.swapi.utilis.Type


@Entity
data class CharacterDataBaseEntity(
    @ColumnInfo val idOnPage: Int,
    @ColumnInfo val name: String,
    @ColumnInfo val height: String,
    @ColumnInfo val mass: String,
    @ColumnInfo val homeworld: String,
    @ColumnInfo var type: Type,
    @ColumnInfo val idList: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    fun mapToCharacterData(): CharacterData {
        return CharacterData(idOnPage, name, height, mass, idList, homeworld, type)
    }
}