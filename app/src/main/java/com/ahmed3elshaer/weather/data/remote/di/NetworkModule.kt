package com.ahmed3elshaer.weather.data.remote.di

import com.ahmed3elshaer.weather.data.remote.WeatherApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
	const val APP_ID = "1a8927de54f779e3daeb1932452a3799"
	private const val BASE_URL = "https://api.openweathermap.org/"

	@Provides
	@Singleton
	fun provideRetrofitInstance(): Retrofit {

		val client = OkHttpClient.Builder()
			.addInterceptor { chain ->
				val original = chain.request()
				val originalHttpUrl = original.url

				val url = originalHttpUrl.newBuilder()
					.addQueryParameter("appid", APP_ID)
					.build()

				val requestBuilder = original.newBuilder()
					.url(url)

				val request = requestBuilder.build()
				chain.proceed(request)
			}
			.addInterceptor(HttpLoggingInterceptor()
				.setLevel(HttpLoggingInterceptor.Level.BODY))
			.build()
		return Retrofit.Builder()
			.baseUrl(BASE_URL)
			.addConverterFactory(GsonConverterFactory.create())
			.client(client)
			.build()
	}

	@Provides
	@Singleton
	fun provideWeatherApi(retrofit: Retrofit): WeatherApi {
		return retrofit.create(WeatherApi::class.java)
	}
}