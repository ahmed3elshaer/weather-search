package com.ahmed3elshaer.weather.domain.model

data class Weather(
	val coord: Coord,
	val weatherDescription: String,
	val temperature: Double,
	val feelsLike: Double,
	val cityName: String
)

data class Coord(
	val lon: Double,
	val lat: Double
)
