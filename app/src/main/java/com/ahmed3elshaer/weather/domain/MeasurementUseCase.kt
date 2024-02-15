package com.ahmed3elshaer.weather.domain

import com.ahmed3elshaer.weather.data.WeatherSearchRepository
import com.ahmed3elshaer.weather.data.storage.model.MeasurementUnit
import javax.inject.Inject

class MeasurementUseCase @Inject constructor(
    private val weatherSearchRepository: WeatherSearchRepository
) {
    fun change(measurementUnit: MeasurementUnit) {
        weatherSearchRepository.changeMeasurementUnit(measurementUnit)
    }

    fun get(): MeasurementUnit = weatherSearchRepository.getMeasurementUnit()

}