package com.alizaidi.trendingrepos.db

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.alizaidi.trendingrepos.data.model.Owner
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    var gson = Gson()

    @TypeConverter
    fun fromOwner(owner: Owner): String {
        return gson.toJson(owner)
    }

    @TypeConverter
    fun toOwner(data: String): Owner {
        val listType = object : TypeToken<Owner>() {
        }.type
        return gson.fromJson(data, listType)
    }
}
