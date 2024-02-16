package com.ahmed3elshaer.weather.data.remote

import com.ahmed3elshaer.weather.data.remote.model.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherApi {

	@GET("data/2.5/weather")
	suspend fun getWeatherByCity(
		@Query("q") city: String,
		@Query("units") units: String


	): Response<WeatherResponse>

	@GET("data/2.5/weather")
	suspend fun getWeatherByLocation(
		@Query("units") units: String,
		@Query("lat") lat: Double,
		@Query("lon") lon: Double


	): Response<WeatherResponse>

}

