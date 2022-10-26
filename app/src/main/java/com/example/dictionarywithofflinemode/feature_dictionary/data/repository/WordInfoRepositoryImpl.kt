package com.example.dictionarywithofflinemode.feature_dictionary.data.repository

import com.example.dictionarywithofflinemode.core.util.Resource
import com.example.dictionarywithofflinemode.feature_dictionary.data.local.WordInfoDoa
import com.example.dictionarywithofflinemode.feature_dictionary.data.remote.DictionaryApi
import com.example.dictionarywithofflinemode.feature_dictionary.domain.model.WordInfo
import com.example.dictionarywithofflinemode.feature_dictionary.domain.repository.WordInfoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class WordInfoRepositoryImpl(
    private val api: DictionaryApi,
    private val dao: WordInfoDoa
) : WordInfoRepository {
    override fun getWordInfo(word: String): Flow<Resource<List<WordInfo>>> = flow {
        emit(Resource.Loading())
        val wordInfos = dao.getWordInfos(word).map { it.toWordInfo() }
        emit(Resource.Loading(data = wordInfos))
        try {
            val remoteWordInfo = api.getWordInfo(word)
            dao.deleteWordInfos(remoteWordInfo.map { it.word })
            dao.insertWordInfos(remoteWordInfo.map { it.toWordInfoEntity() })
        } catch (e: HttpException) {
            emit(
                Resource.Error(
                    message = e.message!!, // Customize your message
                    data = wordInfos
                )
            )
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    message = e.message!!, // Customize your message
                    data = wordInfos
                )
            )
        } catch (e: Exception) {
            emit(
                Resource.Error(
                    message = e.message!!, // Customize your message
                    data = wordInfos
                )
            )
        }
        val newWord = dao.getWordInfos(word).map { it.toWordInfo() }
        emit(Resource.Success(newWord))
    }
}