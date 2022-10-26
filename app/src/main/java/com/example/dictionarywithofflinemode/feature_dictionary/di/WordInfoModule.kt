package com.example.dictionarywithofflinemode.feature_dictionary.di

import android.app.Application
import androidx.room.Room
import com.example.dictionarywithofflinemode.feature_dictionary.data.local.WordInfoDatabase
import com.example.dictionarywithofflinemode.feature_dictionary.data.local.WordInfoDoa
import com.example.dictionarywithofflinemode.feature_dictionary.data.local.entity.Converters
import com.example.dictionarywithofflinemode.feature_dictionary.data.remote.DictionaryApi
import com.example.dictionarywithofflinemode.feature_dictionary.data.repository.WordInfoRepositoryImpl
import com.example.dictionarywithofflinemode.feature_dictionary.data.util.GsonParser
import com.example.dictionarywithofflinemode.feature_dictionary.domain.repository.WordInfoRepository
import com.example.dictionarywithofflinemode.feature_dictionary.domain.use_case.GetWordInfoUseCase
import com.google.gson.Gson
import com.google.gson.JsonParser
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WordInfoModule {

    @Provides
    @Singleton
    fun provideGetWordInfoUseCase(repository: WordInfoRepository): GetWordInfoUseCase {
        return GetWordInfoUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideWordInfoRepository(
        api: DictionaryApi,
        db: WordInfoDatabase
    ): WordInfoRepository {
        return WordInfoRepositoryImpl(api, db.doa)
    }

    @Provides
    @Singleton
    fun providesWordInfoData(application: Application, converters: Converters): WordInfoDatabase {
        return Room
            .databaseBuilder(
                application,
                WordInfoDatabase::class.java,
                "wordinfo_database"
            )
            .addTypeConverter(converters)
            .build()
    }

    @Provides
    @Singleton
    fun providesConverter(jsonParser: com.example.dictionarywithofflinemode.feature_dictionary.data.util.JsonParser) : Converters{
        return Converters(jsonParser)
    }
    @Provides
    @Singleton
    fun providesGsonParser() : GsonParser{
        return GsonParser(Gson())
    }

    @Provides
    @Singleton
    fun provideJsonParser(gsonParser: GsonParser): com.example.dictionarywithofflinemode.feature_dictionary.data.util.JsonParser {
        return gsonParser
    }

    @Provides
    @Singleton
    fun providesDictionaryApi(): DictionaryApi {
        return Retrofit
            .Builder()
            .baseUrl(DictionaryApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DictionaryApi::class.java)
    }

}