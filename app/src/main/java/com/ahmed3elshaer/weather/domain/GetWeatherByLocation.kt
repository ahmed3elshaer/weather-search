package com.ahmed3elshaer.weather.domain

import com.ahmed3elshaer.weather.data.location.LocationProvider
import com.ahmed3elshaer.weather.data.WeatherSearchRepository
import com.ahmed3elshaer.weather.domain.model.Weather
import javax.inject.Inject

class GetWeatherByLocation @Inject constructor(
    private val weatherSearchRepository: WeatherSearchRepository,
    private val locationProvider: LocationProvider
) {
    suspend operator fun invoke(): Result<Weather> {
        return try {
            val location = locationProvider.getCurrentLocation()
            weatherSearchRepository.searchWithLocation(location.latitude, location.longitude)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}