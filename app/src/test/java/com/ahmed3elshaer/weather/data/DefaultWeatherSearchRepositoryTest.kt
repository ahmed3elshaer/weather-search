package com.ahmed3elshaer.weather.data

import com.ahmed3elshaer.weather.core.FakeDispatcher
import com.ahmed3elshaer.weather.data.remote.WeatherApi
import com.ahmed3elshaer.weather.data.remote.model.WeatherResponse
import com.ahmed3elshaer.weather.data.storage.UnitPreference
import com.ahmed3elshaer.weather.data.storage.model.MeasurementUnit
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import retrofit2.Response

class DefaultWeatherSearchRepositoryTest {

    private val fakeDispatcher = FakeDispatcher()

    @Mock
    private lateinit var weatherApi: WeatherApi

    @Mock
    private lateinit var unitPreference: UnitPreference

    private lateinit var repository: DefaultWeatherSearchRepository

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)

        repository = DefaultWeatherSearchRepository(
            weatherApi,
            unitPreference,
            fakeDispatcher
        )

        `when`(unitPreference.getUnit()).thenReturn(flowOf(MeasurementUnit.Metric))
    }

    @Test
    fun `test search with city success`() = runTest(fakeDispatcher.io()) {
        val cityName = "Cairo"
        val weather = WeatherResponse(
            coord = WeatherResponse.Coord(
                lon = 0.0,
                lat = 0.0
            ), weather = listOf(), main = WeatherResponse.Main(
                temp = 0.0,
                feelsLikeTemp = 0.0
            ), name = "city"

        )

        `when`(
            weatherApi.getWeatherByCity(
                cityName,
                units = MeasurementUnit.Metric.name.lowercase()
            )
        ).thenReturn(
            Response.success(weather)
        )

        val result = repository.searchWithCity(cityName)

        assert(result.isSuccess)
        assertThat(result.getOrNull()?.cityName).isEqualTo(weather.name)
    }

    @Test
    fun `test search with city failure`() = runTest(fakeDispatcher.io()) {
        val cityName = "InvalidCityName"
        `when`(
            weatherApi.getWeatherByCity(
                cityName,
                units = MeasurementUnit.Metric.name.lowercase()
            )
        ).thenReturn(
            Response.error(
                404, ResponseBody.create(
                    null,
                    "Invalid city name"

                )
            )
        )

        val result = repository.searchWithCity(cityName)

        assert(result.isFailure)
    }

    @Test
    fun `test search with location success`() = runTest(fakeDispatcher.io()) {
        val lat = 30.0626
        val lon = 31.2497
        val weather = WeatherResponse(
            coord = WeatherResponse.Coord(
                lon = 0.0,
                lat = 0.0
            ), weather = listOf(), main = WeatherResponse.Main(
                temp = 0.0,
                feelsLikeTemp = 0.0
            ), name = "city"

        )
        `when`(
            weatherApi.getWeatherByLocation(
                lat = lat,
                lon = lon,
                units = MeasurementUnit.Metric.name.lowercase(),
            )
        ).thenReturn(
            Response.success(weather)
        )

        val result = repository.searchWithLocation(lat, lon)

        assert(result.isSuccess)
        assertThat(result.getOrNull()?.cityName).isEqualTo(weather.name)
    }

    @Test
    fun `test search with location failure`() = runTest(fakeDispatcher.io()) {
        val lat = -900.0 // invalid latitude
        val lon = 0.0
        `when`(
            weatherApi.getWeatherByLocation(
                lat = lat,
                lon = lon,
                units = MeasurementUnit.Metric.name.lowercase(),
            )
        ).thenReturn(
            Response.error(
                404, ResponseBody.create(
                    null,
                    "Invalid city name"

                )
            )

        )

        val result = repository.searchWithLocation(lat, lon)

        assert(result.isFailure)
    }
}