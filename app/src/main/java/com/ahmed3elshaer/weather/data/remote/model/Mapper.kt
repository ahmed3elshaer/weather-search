package com.ahmed3elshaer.weather.data.remote.model

import com.ahmed3elshaer.weather.domain.model.Coord
import com.ahmed3elshaer.weather.domain.model.Weather
import retrofit2.Response

fun WeatherResponse.mapToDomainModel(): Weather {
    return Weather(
        coord = Coord(
            lon = coord.lon,
            lat = coord.lat
        ),
        weatherDescription = weather.firstOrNull()?.conditionDescription ?: "",
        temperature = main.temp,
        feelsLike = main.feelsLikeTemp,
        cityName = name
    )
}

fun <T> Response<T>.unwrapResponse(): Result<T> {
    return when (code()) {
        in 200..299 -> Result.success(body()!!)
        else -> Result.failure(Exception(errorBody()?.string() ?: "Unknown error"))
    }
}