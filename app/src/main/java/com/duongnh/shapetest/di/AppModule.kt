package com.duongnh.shapetest.di

import com.duongnh.shapetest.data.data_source.remote.api.ColourApi
import com.duongnh.shapetest.data.repository.ColourRepositoryImpl
import com.duongnh.shapetest.domain.repository.ColourRepository
import com.duongnh.shapetest.domain.use_case.GetRandomColorUseCase
import com.duongnh.shapetest.domain.use_case.GetRandomImageUrlUseCase
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

const val BASE_URL = "http://www.colourlovers.com/api/"
private const val CONNECT_TIMEOUT = 60L
private const val WRITE_TIMEOUT = 60L
private const val READ_TIMEOUT = 60L
private const val CONTENT_TYPE_NAME = "Content-Type"
private const val CONTENT_TYPE_VALUE = "application/json"

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofitClient(
        gsonConverterFactory: GsonConverterFactory,
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder().apply {
            baseUrl(BASE_URL)
            addConverterFactory(gsonConverterFactory)
            client(okHttpClient)
        }.build()
    }

    @Provides
    @Singleton
    fun provideRetrofitHttpClient(headerInterceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder().apply {
            connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            retryOnConnectionFailure(true)
            addInterceptor(headerInterceptor)
            addNetworkInterceptor(HttpLoggingInterceptor().apply { setLevel(HttpLoggingInterceptor.Level.BODY) })
        }.build()
    }

    @Provides
    @Singleton
    fun provideHeaderInterceptor(): Interceptor {
        return Interceptor { chain ->
            val original = chain.request()

            val request = original.newBuilder()
                .header(CONTENT_TYPE_NAME, CONTENT_TYPE_VALUE)
                .method(original.method, original.body)
                .build()

            chain.proceed(request)
        }
    }

    @Provides
    @Singleton
    fun provideGsonBuilder(): Gson {
        return GsonBuilder().create()
    }

    @Provides
    @Singleton
    fun provideGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Provides
    @Singleton
    fun provideColourApi(retrofit: Retrofit): ColourApi {
        return retrofit.create(ColourApi::class.java)
    }

    @Provides
    @Singleton
    fun provideColourRepository(colourApi: ColourApi): ColourRepository {
        return ColourRepositoryImpl(colourApi)
    }

    @Provides
    fun provideGetRandomColorUseCase(colourRepository: ColourRepository): GetRandomColorUseCase {
        return GetRandomColorUseCase(colourRepository)
    }

    @Provides
    fun provideGetRandomImageUrlUseCase(colourRepository: ColourRepository): GetRandomImageUrlUseCase {
        return GetRandomImageUrlUseCase(colourRepository)
    }
}