package com.ahmed3elshaer.weather.data

import com.ahmed3elshaer.weather.data.storage.model.MeasurementUnit
import com.ahmed3elshaer.weather.domain.model.Coord
import com.ahmed3elshaer.weather.domain.model.Weather
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class FakeWeatherRepository @Inject constructor() : WeatherSearchRepository {
    override suspend fun searchWithCity(name: String): Result<Weather> {
        return Result.success(
            Weather(
                coord = Coord(lon = 0.0, lat = 0.0),
                weatherDescription = "",
                temperature = 0.0,
                feelsLike = 0.0,
                cityName = "London"
            )
        )
    }

    override suspend fun searchWithLocation(lat: Double, lng: Double): Result<Weather> {
        return Result.success(
            Weather(
                coord = Coord(lon = 0.0, lat = 0.0),
                weatherDescription = "",
                temperature = 0.0,
                feelsLike = 0.0,
                cityName = "Cairo"
            )
        )
    }

    override fun changeMeasurementUnit(measurementUnit: MeasurementUnit) {
        // no-op
    }

    override fun getMeasurementUnit(): Flow<MeasurementUnit> {
        return flowOf(MeasurementUnit.Metric)
    }
}