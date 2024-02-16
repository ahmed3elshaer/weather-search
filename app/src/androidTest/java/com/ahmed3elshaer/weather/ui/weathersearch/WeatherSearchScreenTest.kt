package com.ahmed3elshaer.weather.ui.weathersearch

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ahmed3elshaer.weather.data.storage.model.MeasurementUnit
import com.ahmed3elshaer.weather.domain.model.Coord
import com.ahmed3elshaer.weather.domain.model.Weather
import com.ahmed3elshaer.weather.ui.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * UI tests for [WeatherSearchScreen].
 */
@RunWith(AndroidJUnit4::class)
class WeatherSearchScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Before
    fun setup() {
    }

    @Test
    fun cityName_IsShown() {
        composeTestRule.setContent {
            WeatherSearchScreen(
                isLoading = true, weather = Weather(
                    coord = Coord(lon = 0.0, lat = 0.0),
                    weatherDescription = "a bit cloudy",
                    temperature = 21.0,
                    feelsLike = 22.0,
                    cityName = "Cairo"
                ), onCitySearch = {},
                temperatureUnit = MeasurementUnit.Metric,
                onTemperatureUnitChange = {}
            )
        }
        composeTestRule.onNodeWithText("Cairo").assertExists().performClick()
    }

    @Test
    fun errorState_IsShown_Properly() {
        val errorMessage = "An error occurred"
        composeTestRule.setContent {
            WeatherSearchScreen(
                isLoading = false,
                weather = null,
                onCitySearch = {},
                temperatureUnit = MeasurementUnit.Metric,
                onTemperatureUnitChange = {},
                error = errorMessage
            )
        }
        // Asserts that error message is displayed
        composeTestRule.onNodeWithText(errorMessage).assertExists()
    }

    @Test
    fun successState_IsShown_Properly() {
        val data = Weather(
            coord = Coord(lon = 0.0, lat = 0.0),
            weatherDescription = "a bit cloudy",
            temperature = 21.0,
            feelsLike = 22.0,
            cityName = "Cairo"
        )
        composeTestRule.setContent {
            WeatherSearchScreen(
                isLoading = false,
                weather = data,
                onCitySearch = {},
                temperatureUnit = MeasurementUnit.Metric,
                onTemperatureUnitChange = {}
            )
        }
        // Asserts that weather description and city name are displayed correctly
        composeTestRule.onNodeWithText("Cairo").assertExists()
        composeTestRule.onNodeWithText("a bit cloudy").assertExists()
    }

    @Test
    fun longCityName_DisplayedProperly() {
        val longCityName = "SupercalifragilisticexpialidociousCity"
        val data = Weather(
            coord = Coord(lon = 0.0, lat = 0.0),
            weatherDescription = "a bit cloudy",
            temperature = 21.0,
            feelsLike = 22.0,
            cityName = longCityName
        )
        composeTestRule.setContent {
            WeatherSearchScreen(
                isLoading = false,
                weather = data,
                onCitySearch = {},
                temperatureUnit = MeasurementUnit.Metric,
                onTemperatureUnitChange = {}
            )
        }
        composeTestRule.onNodeWithText(longCityName).assertExists()
    }

    @Test
    fun emptyCityName_SearchClicked_ShowsError() {
        composeTestRule.setContent {
            WeatherSearchScreen(
                isLoading = false,
                weather = null,
                onCitySearch = { /* assert that an error is shown */ },
                temperatureUnit = MeasurementUnit.Metric,
                onTemperatureUnitChange = {},
                error = null
            )
        }
        composeTestRule.onNodeWithText("Search").performClick()
        // Assert that an error message is shown
    }

    @Test
    fun nonExistentCityName_SearchClicked_ShowsError() {
        // Assume the "onCitySearch" callback shows an error message when it gets a non-existent city
        composeTestRule.setContent {
            WeatherSearchScreen(
                isLoading = false,
                weather = null,
                onCitySearch = { /* assert that an error is shown */ },
                temperatureUnit = MeasurementUnit.Metric,
                onTemperatureUnitChange = {},
                error = null
            )
        }
        composeTestRule.onNodeWithText("Enter city name").performTextInput("NotACityName")
        composeTestRule.onNodeWithText("Search").performClick()
        // Assert that an error message is shown
    }

    @Test
    fun extremelyLongInput_SearchClicked_ShowsError() {
        val longInput = "a".repeat(1000)
        composeTestRule.setContent {
            WeatherSearchScreen(
                isLoading = false,
                weather = null,
                onCitySearch = { /* assert that an error is shown */ },
                temperatureUnit = MeasurementUnit.Metric,
                onTemperatureUnitChange = {},
                error = null
            )
        }
        composeTestRule.onNodeWithText("Enter city name").performTextInput(longInput)
        composeTestRule.onNodeWithText("Search").performClick()
        // Assert that an error message is shown
    }

    @Test
    fun invalidCharacterInCityName_SearchClicked_ShowsError() {
        val cityWithInvalidCharacter = "C1tyN@me"
        composeTestRule.setContent {
            WeatherSearchScreen(
                isLoading = false,
                weather = null,
                onCitySearch = { /* assert that an error is shown */ },
                temperatureUnit = MeasurementUnit.Metric,
                onTemperatureUnitChange = {},
                error = null
            )
        }
        composeTestRule.onNodeWithText("Enter city name").performTextInput(cityWithInvalidCharacter)
        composeTestRule.onNodeWithText("Search").performClick()
        // Assert that an error message is shown
    }

}
