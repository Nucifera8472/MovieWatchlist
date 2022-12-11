package com.holudi.moviewatchlist.injection.modules

import android.content.Context
import com.holudi.moviewatchlist.injection.JSON
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import timber.log.Timber
import javax.inject.Qualifier
import javax.inject.Singleton

@OptIn(ExperimentalSerializationApi::class)
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    enum class ContentTypes(val type: String) {
        JSON("application/json");

        fun toMediaType(): MediaType = type.toMediaType()
    }

    @Provides
    @LoggingOkHttpClient
    fun provideLoggingOkHttpClient(@BaseOkHttpClient baseClient: OkHttpClient): OkHttpClient {
        return baseClient.newBuilder()
            .addInterceptor(defaultLoggingInterceptor())
            .build()
    }

    @Provides
    @Singleton
    @BaseOkHttpClient
    fun provideBaseHttpClient(@ApplicationContext context: Context): OkHttpClient {
        return OkHttpClient.Builder().build()
    }

    @Provides
    @Singleton
    @RetrofitOkHttpClient
    fun provideRetrofitOkHttpClient(
        @BaseOkHttpClient baseOkHttpClient: OkHttpClient
    ): OkHttpClient {
        val builder = baseOkHttpClient.newBuilder()
        builder.addInterceptor(defaultLoggingInterceptor())
        return builder.build()
    }

    private fun defaultLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor { message -> Timber.tag("-- OkHttp").d(message) }
        logging.level = HttpLoggingInterceptor.Level.BODY
        logging.redactHeader("Authorization")
        return logging
    }

    @Provides
    @Singleton
    fun provideJsonConverterFactory(): Converter.Factory {
        return JSON.asConverterFactory(ContentTypes.JSON.toMediaType())
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BaseOkHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LoggingOkHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class PlayerOkHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RetrofitOkHttpClient
