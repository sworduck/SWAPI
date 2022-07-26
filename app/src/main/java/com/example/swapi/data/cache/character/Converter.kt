package com.example.swapi.data.cache.character

import androidx.room.TypeConverter
import com.example.swapi.utilis.Type

class Converter {
    @TypeConverter
    fun fromPriority(type: Type): String {
        return type.name
    }

    @TypeConverter
    fun toPriority(type: String): Type {
        return Type.valueOf(type)
    }

}