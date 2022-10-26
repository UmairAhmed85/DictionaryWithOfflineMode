package com.example.dictionarywithofflinemode.feature_dictionary.domain.repository

import com.example.dictionarywithofflinemode.core.util.Resource
import com.example.dictionarywithofflinemode.feature_dictionary.domain.model.WordInfo
import kotlinx.coroutines.flow.Flow

interface WordInfoRepository {
    fun getWordInfo(word : String): Flow<Resource<List<WordInfo>>>
}