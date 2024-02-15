package com.ahmed3elshaer.weather.domain

import com.ahmed3elshaer.weather.data.WeatherSearchRepository
import com.ahmed3elshaer.weather.domain.model.Weather
import javax.inject.Inject

class GetWeatherByCityUseCase @Inject constructor(
    private val weatherSearchRepository: WeatherSearchRepository
) {
    suspend operator fun invoke(name: String): Result<Weather> {
        return weatherSearchRepository.searchWithCity(name)
    }
}