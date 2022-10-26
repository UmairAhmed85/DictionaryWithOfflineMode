package com.example.dictionarywithofflinemode.feature_dictionary.data.local.entity

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.example.dictionarywithofflinemode.feature_dictionary.data.util.JsonParser
import com.example.dictionarywithofflinemode.feature_dictionary.domain.model.Meaning
import com.google.gson.reflect.TypeToken
import javax.inject.Inject

@ProvidedTypeConverter
class Converters @Inject constructor(
    private val jsonParser: JsonParser
) {
    @TypeConverter
    fun fromMeaningJson(json: String): List<Meaning> {
        return jsonParser.fromJson<ArrayList<Meaning>>(
            json,
            object : TypeToken<ArrayList<Meaning>>() {}.type
        ) ?: emptyList()
    }

    @TypeConverter
    fun toMeaningsJson(meaning: List<Meaning>): String{
        return jsonParser.toJson(
            meaning,
            object : TypeToken<ArrayList<Meaning>>() {}.type
        )?:"[]"
    }
}