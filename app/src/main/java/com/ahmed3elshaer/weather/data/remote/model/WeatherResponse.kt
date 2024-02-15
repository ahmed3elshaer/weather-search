package com.ahmed3elshaer.weather.data.remote.model

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
	@SerializedName("coord") val coord: Coord,
	@SerializedName("weather") val weather: List<WeatherCondition>,
	@SerializedName("main") val main: Main,
	@SerializedName("name") val name: String
) {
	data class Coord(
		@SerializedName("lon") val lon: Double,
		@SerializedName("lat") val lat: Double
	)

	data class WeatherCondition(
		@SerializedName("main") val mainCondition: String,
		@SerializedName("description") val conditionDescription: String
	)

	data class Main(
		@SerializedName("temp") val temp: Double,
		@SerializedName("feels_like") val feelsLikeTemp: Double
	)
}
