package com.ahmed3elshaer.weather.data.remote.di

import com.ahmed3elshaer.weather.data.remote.WeatherApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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
		return Retrofit.Builder()
			.baseUrl(BASE_URL)
			.addConverterFactory(GsonConverterFactory.create())
			.build()
	}

	@Provides
	@Singleton
	fun provideWeatherApi(retrofit: Retrofit): WeatherApi {
		return retrofit.create(WeatherApi::class.java)
	}
}