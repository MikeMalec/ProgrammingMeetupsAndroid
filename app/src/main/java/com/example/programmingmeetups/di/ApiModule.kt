package com.example.programmingmeetups.di

import com.example.programmingmeetups.framework.datasource.network.auth.data.request.RequestBodyFactoryImpl
import com.example.programmingmeetups.framework.datasource.network.auth.data.request.RequestBodyFactoryInterface
import com.example.programmingmeetups.utils.BASE_URL
import com.example.programmingmeetups.utils.files.FileManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object ApiModule {
    @Singleton
    @Provides
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson): Retrofit.Builder {
        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
    }

    @RequestBodyFactoryImplementation
    @Singleton
    @Provides
    fun provideRequestBodyFactoryImpl(fileManager: FileManager): RequestBodyFactoryInterface {
        return RequestBodyFactoryImpl(fileManager)
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RequestBodyFactoryImplementation