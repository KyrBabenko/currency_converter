package com.developers.currency_exchange.network

import com.developers.currency_exchange.BuildConfig
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    private const val READ_TIMEOUT = 10L
    private const val WRITE_TIMEOUT = 10L

    @Provides
    fun provideGson(): Gson {
        return GsonBuilder()
            .setLenient()
            .create()
    }

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient().newBuilder()

        if (BuildConfig.DEBUG) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            okHttpClientBuilder.addInterceptor(interceptor)
        }
        okHttpClientBuilder.readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
        okHttpClientBuilder.writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)

        return okHttpClientBuilder.build()
    }

    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gson: Gson
    ): Retrofit {
        val retrofitBuilder = Retrofit.Builder()

        retrofitBuilder.baseUrl(getBaseUrl())
        retrofitBuilder.client(okHttpClient)
        val gsonConverterFactory = GsonConverterFactory.create(gson)
        retrofitBuilder.addConverterFactory(gsonConverterFactory)
        return retrofitBuilder.build()
    }

    private fun getBaseUrl(): String {
        val baseURL = BuildConfig.BASE_URL
        val result = StringBuilder()

        for (char in baseURL) {
            if (char.isLetter()) {
                val start = if (char.isUpperCase()) 'A' else 'a'
                val decodedChar = (start + (char - start - 3 + 26) % 26)
                result.append(decodedChar)
            } else {
                result.append(char)
            }
        }

        return result.toString()
    }
}
