package com.ahmed3elshaer.weather.ui.weathersearch

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ahmed3elshaer.weather.domain.model.Coord
import com.ahmed3elshaer.weather.domain.model.Weather
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
        composeTestRule.setContent {
            WeatherSearchScreen(weather = FAKE_DATA, onCitySearch = {})
        }
    }

    @Test
    fun firstItem_exists() {
        composeTestRule.onNodeWithText(FAKE_DATA.cityName).assertExists().performClick()
    }
}

private val FAKE_DATA = Weather(
    coord = Coord(lon = 0.0, lat = 0.0),
    weatherDescription = "",
    temperature = 0.0,
    feelsLike = 0.0,
    cityName = "Cairo"
)
