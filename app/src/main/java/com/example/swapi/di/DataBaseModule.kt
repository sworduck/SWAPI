package com.example.swapi.di

import android.content.Context
import androidx.room.Room
import com.example.swapi.data.cache.BaseCacheDataSource
import com.example.swapi.data.cache.SwapiRoomDataBase
import com.example.swapi.data.cloud.ApiService
import com.example.swapi.data.cloud.BaseCloudDataSource
import com.example.swapi.domain.FavoriteUseCase
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
    fun provideDataBase(@ApplicationContext context: Context): SwapiRoomDataBase {
        return Room.databaseBuilder(
            context,
            SwapiRoomDataBase::class.java,
            SwapiRoomDataBase.DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideCacheDataSource(db: SwapiRoomDataBase): BaseCacheDataSource {
        return BaseCacheDataSource(db)
    }

    @Provides
    @Singleton
    fun provideSearchFragmentAdapter(cacheDataSource: BaseCacheDataSource): FavoriteUseCase {
        return FavoriteUseCase(cacheDataSource)
    }

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(ApiService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideCloudDataSource(service: ApiService): BaseCloudDataSource {
        return BaseCloudDataSource(service)
    }
}