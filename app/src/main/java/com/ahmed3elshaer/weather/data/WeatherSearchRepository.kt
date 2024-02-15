package com.ahmed3elshaer.weather.data

import com.ahmed3elshaer.weather.core.DispatcherProvider
import com.ahmed3elshaer.weather.data.remote.WeatherApi
import com.ahmed3elshaer.weather.data.remote.model.mapToDomainModel
import com.ahmed3elshaer.weather.data.remote.model.unwrapResponse
import com.ahmed3elshaer.weather.data.storage.UnitPreference
import com.ahmed3elshaer.weather.data.storage.model.MeasurementUnit
import com.ahmed3elshaer.weather.domain.model.Weather
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface WeatherSearchRepository {
    suspend fun searchWithCity(
        name: String
    ): Result<Weather>

    suspend fun searchWithLocation(
        lat: Double,
        lng: Double
    ): Result<Weather>

    fun changeMeasurementUnit(measurementUnit: MeasurementUnit)
    fun getMeasurementUnit() : MeasurementUnit
}

class DefaultWeatherSearchRepository @Inject constructor(
    private val weatherApi: WeatherApi,
    private val unitPreference: UnitPreference,
    private val dispatcherProvider: DispatcherProvider
) : WeatherSearchRepository {

    override suspend fun searchWithCity(name: String): Result<Weather> {
        return withContext(dispatcherProvider.io()) {
            val unit = unitPreference.getUnit()
            weatherApi.getWeatherByCity(
                city = name,
                units = unit.name
            ).unwrapResponse()
                .map { response ->
                    response.mapToDomainModel()
                }
        }
    }

    override suspend fun searchWithLocation(lat: Double, lng: Double): Result<Weather> {
        return withContext(dispatcherProvider.io()) {
            val unit = unitPreference.getUnit()
            weatherApi.getWeatherByLocation(
                lat = lat,
                lon = lng,
                units = unit.name
            ).unwrapResponse()
                .map { response ->
                    response.mapToDomainModel()
                }
        }
    }

    override fun changeMeasurementUnit(measurementUnit: MeasurementUnit) {
        unitPreference.saveUnit(measurementUnit)
    }

    override fun getMeasurementUnit(): MeasurementUnit {
        return unitPreference.getUnit()
    }
}
