package com.example.dictionarywithofflinemode.feature_dictionary.data.local

import androidx.room.*
import com.example.dictionarywithofflinemode.feature_dictionary.data.local.entity.WordInfoEntity

@Dao
interface WordInfoDoa {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWordInfos(infos : List<WordInfoEntity>)

    @Query("DELETE FROM wordinfoentity WHERE word IN(:words)")
    suspend fun deleteWordInfos(words : List<String>)

    @Query("SELECT * FROM wordinfoentity WHERE word LIKE '%' || :word ||'%'")
    suspend fun getWordInfos(word : String) : List<WordInfoEntity>
}