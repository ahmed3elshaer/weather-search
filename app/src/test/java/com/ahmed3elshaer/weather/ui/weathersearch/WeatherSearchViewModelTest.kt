/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ahmed3elshaer.weather.ui.weathersearch


import com.ahmed3elshaer.weather.data.WeatherSearchRepository
import com.ahmed3elshaer.weather.data.storage.model.MeasurementUnit
import com.ahmed3elshaer.weather.domain.model.Coord
import com.ahmed3elshaer.weather.domain.model.Weather
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@OptIn(ExperimentalCoroutinesApi::class) // TODO: Remove when stable
class WeatherSearchViewModelTest {

}

private class FakeWeatherSearchRepository : WeatherSearchRepository {
    override suspend fun searchWithCity(name: String): Result<Weather> {
        return Result.success(
            Weather(
                coord = Coord(lon = 0.0, lat = 0.0),
                weatherDescription = "",
                temperature = 0.0,
                feelsLike = 0.0,
                cityName = ""
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
                cityName = ""
            )
        )
    }

    override fun changeMeasurementUnit(measurementUnit: MeasurementUnit) {
        // no-op
    }

    override fun getMeasurementUnit(): MeasurementUnit {
        return MeasurementUnit.CELSIUS
    }

}
