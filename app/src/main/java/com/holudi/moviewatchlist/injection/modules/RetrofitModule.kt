package com.holudi.moviewatchlist.injection.modules

import com.holudi.moviewatchlist.data.remote.omdb.OmdbApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    private const val BASE_URL = "https://www.omdbapi.com"

    @Provides
    @Singleton
    fun provideRetrofit(
        @RetrofitOkHttpClient okHttpClient: OkHttpClient,
        jsonConverterFactory: Converter.Factory
    ): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(jsonConverterFactory)
            .build()
    }

    @Provides
    @Singleton
    fun provideOmdbApi(retrofit: Retrofit): OmdbApi =
        retrofit.create(OmdbApi::class.java)

}
