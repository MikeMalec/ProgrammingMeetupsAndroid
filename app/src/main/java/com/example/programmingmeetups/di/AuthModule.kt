package com.example.programmingmeetups.di

import com.example.programmingmeetups.business.data.network.auth.AuthRepository
import com.example.programmingmeetups.business.data.network.auth.AuthRepositoryImpl
import com.example.programmingmeetups.business.interactors.auth.AuthInteractors
import com.example.programmingmeetups.business.interactors.auth.Login
import com.example.programmingmeetups.business.interactors.auth.Register
import com.example.programmingmeetups.framework.datasource.network.auth.AuthService
import com.example.programmingmeetups.framework.datasource.network.auth.AuthServiceImpl
import com.example.programmingmeetups.framework.datasource.network.auth.data.AuthApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AuthModule {
    @Singleton
    @Provides
    fun provideAuthApi(retrofit: Retrofit.Builder): AuthApi {
        return retrofit.build().create(AuthApi::class.java)
    }

    @AuthServiceImplementation
    @Singleton
    @Provides
    fun provideAuthService(authApi: AuthApi): AuthService {
        return AuthServiceImpl(authApi)
    }

    @AuthRepositoryImplementation
    @Singleton
    @Provides
    fun provideAuthRepository(@AuthServiceImplementation authService: AuthService): AuthRepository {
        return AuthRepositoryImpl(authService)
    }

    @Singleton
    @Provides
    fun provideRegister(@AuthRepositoryImplementation authRepository: AuthRepository): Register {
        return Register(authRepository)
    }

    @Singleton
    @Provides
    fun provideLogin(@AuthRepositoryImplementation authRepository: AuthRepository): Login {
        return Login(authRepository)
    }

    @Singleton
    @Provides
    fun provideAuthInteractors(register: Register, login: Login): AuthInteractors {
        return AuthInteractors(register, login)
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthServiceImplementation

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthRepositoryImplementation