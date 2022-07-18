package com.example.swapi.di

import android.content.Context
import androidx.room.Room
import com.example.swapi.api.ApiService
import com.example.swapi.data.cache.CharacterCacheDataSource
import com.example.swapi.data.cache.CharacterRoomDataBase
import com.example.swapi.data.cloud.CharacterListFromCloud
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {

    @Provides
    @Singleton
    fun provideDataBase(@ApplicationContext context: Context):CharacterRoomDataBase{
        return Room.databaseBuilder(
            context,
            CharacterRoomDataBase::class.java,
            CharacterRoomDataBase.DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideCacheDataSource(db: CharacterRoomDataBase): CharacterCacheDataSource {
        return CharacterCacheDataSource.BaseRoom(db)
    }

    @Singleton
    @Provides
    fun provideRetrofit():Retrofit =
        Retrofit.Builder()
            .baseUrl("https://swapi.dev/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideCloudDataSource(service: ApiService): CharacterListFromCloud {
        return CharacterListFromCloud.Base(service)
    }

}