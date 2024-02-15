package com.ahmed3elshaer.weather.ui.weathersearch

import com.ahmed3elshaer.weather.ui.theme.MyApplicationTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmed3elshaer.weather.domain.model.Coord
import com.ahmed3elshaer.weather.domain.model.Weather

@Composable
fun WeatherSearchScreen(
    modifier: Modifier = Modifier,
    viewModel: WeatherSearchViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    WeatherSearchScreen(
        weather = state.weather,
        onCitySearch = { viewModel.onUserEvent(WeatherSearchUserEvent.SearchByCity(it)) },
        modifier = modifier
    )
}

@Composable
internal fun WeatherSearchScreen(
    modifier: Modifier = Modifier,
    weather: Weather?,
    isLoading: Boolean = false,
    error: String? = null,
    onCitySearch: (name: String) -> Unit,
) {
    Column(
        modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var nameWeatherSearch by remember { mutableStateOf("") }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TextField(
                value = nameWeatherSearch,
                onValueChange = { nameWeatherSearch = it }
            )

            Button(
                modifier = Modifier.width(96.dp),
                onClick = { onCitySearch(nameWeatherSearch) }) {
                Text("Search")
            }
        }
        if (isLoading) {
            LinearProgressIndicator()
        }
        if (weather != null) {
            WeatherDetails(weather = weather)
        }
        if (error != null) {
            Text(
                color = MaterialTheme.colorScheme.error,
                fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                text = error,
            )
        }

    }
}

@Composable
fun WeatherDetails(weather: Weather) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "City: ${weather.cityName}")
        Text(text = "Weather: ${weather.weatherDescription}")
        Text(text = "Temperature: ${weather.temperature}")
        Text(text = "Feels like: ${weather.feelsLike}")
    }
}

// Previews

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    MyApplicationTheme {
        WeatherSearchScreen(
            weather = Weather(
                coord = Coord(lon = 0.0, lat = 0.0),
                weatherDescription = "a bit cloudy",
                temperature = 21.0,
                feelsLike = 22.0,
                cityName = "Cairo"
            ), onCitySearch = {})
    }
}

@Preview(showBackground = true, widthDp = 480)
@Composable
private fun PortraitPreview() {
    MyApplicationTheme {
        WeatherSearchScreen(
            weather = Weather(
                coord = Coord(lon = 0.0, lat = 0.0),
                weatherDescription = "a bit cloudy",
                temperature = 21.0,
                feelsLike = 22.0,
                cityName = "Cairo"
            ), onCitySearch = {})
    }
}

@Preview(showBackground = true, widthDp = 480)
@Composable
private fun LoadingPortraitPreview() {
    MyApplicationTheme {
        WeatherSearchScreen(
            isLoading = true,
            weather = Weather(
                coord = Coord(lon = 0.0, lat = 0.0),
                weatherDescription = "a bit cloudy",
                temperature = 21.0,
                feelsLike = 22.0,
                cityName = "Cairo"
            ), onCitySearch = {})
    }
}

@Preview(showBackground = true, widthDp = 480)
@Composable
private fun ErrorPortraitPreview() {
    MyApplicationTheme {
        WeatherSearchScreen(
            error = "City not found",
            weather = Weather(
                coord = Coord(lon = 0.0, lat = 0.0),
                weatherDescription = "a bit cloudy",
                temperature = 21.0,
                feelsLike = 22.0,
                cityName = "Cairo"
            ),
            onCitySearch = {}
        )
    }
}
