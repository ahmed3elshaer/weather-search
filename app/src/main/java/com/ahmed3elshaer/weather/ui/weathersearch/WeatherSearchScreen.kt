@file:OptIn(ExperimentalPermissionsApi::class)

package com.ahmed3elshaer.weather.ui.weathersearch

import android.Manifest
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.Waves
import androidx.compose.material.icons.outlined.Cloud
import androidx.compose.material.icons.outlined.LocationCity
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmed3elshaer.weather.data.storage.model.MeasurementUnit
import com.ahmed3elshaer.weather.domain.model.Coord
import com.ahmed3elshaer.weather.domain.model.Weather
import com.ahmed3elshaer.weather.ui.theme.MyApplicationTheme
import com.google.accompanist.permissions.*


@Composable
fun WeatherSearchScreen(
    modifier: Modifier = Modifier, viewModel: WeatherSearchViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val selectedUnit by viewModel.measurementUnit.collectAsStateWithLifecycle()
    Log.d("WeatherSearchScreen", "WeatherSearchScreen: $state")
    RequestLocationPermission(onPermissionGranted = {
        viewModel.onUserEvent(WeatherSearchUserEvent.SearchByLocation)
    })
    WeatherSearchScreen(
        modifier = modifier,
        weather = state.weather,
        isLoading = state.isLoading,
        error = state.error,
        isEmpty = state.isEmpty,
        inputFieldError = state.inputFieldError,
        onCitySearch = { viewModel.onUserEvent(WeatherSearchUserEvent.SearchByCity(it)) },
        onTemperatureUnitChange = { viewModel.changeMeasurementUnit(it) },
        temperatureUnit = selectedUnit
    )
}

@Composable
@OptIn(ExperimentalPermissionsApi::class)
private fun RequestLocationPermission(onPermissionGranted: () -> Unit) {
    val permissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )
    val showRationalDialog = rememberSaveable { mutableStateOf(false) }
    val permissionHandled = rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(permissionState) {
        if (permissionState.shouldShowRationale) {
            showRationalDialog.value = true
        } else {
            showRationalDialog.value = false
            permissionState.launchMultiplePermissionRequest()
        }
    }
    LaunchedEffect(permissionState.allPermissionsGranted) {
        if (permissionState.allPermissionsGranted && !permissionHandled.value) {
            permissionHandled.value = true
            onPermissionGranted()
        }

    }
    RationalDialog(showRationalDialog) {
        permissionState.launchMultiplePermissionRequest()
    }
}

@Composable
fun RationalDialog(
    showRationalDialog: MutableState<Boolean>, onRequestPermission: () -> Unit = { }
) {
    if (showRationalDialog.value) {
        AlertDialog(
            onDismissRequest = {
                showRationalDialog.value = false
            },
            title = {
                Text(
                    text = "Permission", fontWeight = FontWeight.Bold, fontSize = 16.sp
                )
            },
            text = {
                Text(
                    "The location is important for this app. Please grant the permission.",
                    fontSize = 16.sp
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    onRequestPermission()
                    showRationalDialog.value = false
                }) {
                    Text("OK", style = TextStyle(color = MaterialTheme.colorScheme.onSurface))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showRationalDialog.value = false
                }) {
                    Text("Cancel", style = TextStyle(color = MaterialTheme.colorScheme.error))
                }
            },
        )

    }
}

@Composable
internal fun WeatherSearchScreen(
    modifier: Modifier = Modifier,
    weather: Weather?,
    isLoading: Boolean = false,
    error: String? = null,
    isEmpty: Boolean = false,
    isRequestInProgress: Boolean = false,
    inputFieldError: String? = null,
    temperatureUnit: MeasurementUnit,
    onTemperatureUnitChange: (MeasurementUnit) -> Unit,
    onCitySearch: (name: String) -> Unit,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var nameWeatherSearch by rememberSaveable { mutableStateOf("") }

        OutlinedTextField(modifier = Modifier.fillMaxWidth(),
            value = nameWeatherSearch,
            onValueChange = { nameWeatherSearch = it },
            label = { Text("Enter city name") },
            isError = inputFieldError != null,
            supportingText = { Text(inputFieldError ?: "") },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Search, contentDescription = "Search"
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.secondary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            ),
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyLarge,
            keyboardActions = KeyboardActions(onSearch = {
                onCitySearch(nameWeatherSearch)
            }))

        TemperatureUnitChips(
            temperatureUnit = temperatureUnit, onTemperatureUnitChange = onTemperatureUnitChange
        )

        Button(modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading,
            onClick = { onCitySearch(nameWeatherSearch) }) {
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                Icon(imageVector = Icons.Filled.Search, contentDescription = "Search")
                Text("Search")
            }
        }
        when {
            weather != null -> WeatherDetails(weather = weather, temperatureUnit = temperatureUnit)
            error != null -> Text(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.error,
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                textAlign = TextAlign.Center,
                text = error
            )

            isEmpty -> Text(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                textAlign = TextAlign.Center,
                text = "City not found"
            )
        }
    }
}

@Composable
private fun TemperatureUnitChips(
    temperatureUnit: MeasurementUnit, onTemperatureUnitChange: (MeasurementUnit) -> Unit
) {
    val temperatureUnitOptions = listOf(
        MeasurementUnit.Standard, MeasurementUnit.Imperial, MeasurementUnit.Metric
    )
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (unit in temperatureUnitOptions) {
            TemperatureUnitChip(
                unit = unit,
                isSelected = unit == temperatureUnit,
                onTemperatureUnitSelected = onTemperatureUnitChange
            )
        }
    }
}

@Composable
private fun TemperatureUnitChip(
    unit: MeasurementUnit, isSelected: Boolean, onTemperatureUnitSelected: (MeasurementUnit) -> Unit
) {
    val chipBackgroundColor =
        if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
    val chipContentColor =
        if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface

    Surface(shape = MaterialTheme.shapes.small,
        color = chipBackgroundColor,
        modifier = Modifier.clickable { onTemperatureUnitSelected(unit) }) {
        Text(
            text = unit.name,
            color = chipContentColor,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        )
    }
}

@Composable
fun WeatherDetails(weather: Weather, temperatureUnit: MeasurementUnit) {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        WeatherDetailRow(
            icon = Icons.Outlined.LocationCity, label = "City:", value = weather.cityName
        )
        WeatherDetailRow(
            icon = Icons.Outlined.Cloud, label = "Weather:", value = weather.weatherDescription
        )
        WeatherDetailRow(
            icon = Icons.Filled.Thermostat,
            label = "Temperature:",
            value = "${weather.temperature} ${getTemperatureUnitAbbreviation(temperatureUnit)}"
        )
        WeatherDetailRow(
            icon = Icons.Filled.Waves,
            label = "Feels like:",
            value = "${weather.feelsLike} ${getTemperatureUnitAbbreviation(temperatureUnit)}"
        )
    }
}

private fun getTemperatureUnitAbbreviation(unit: MeasurementUnit): String {
    return when (unit) {
        MeasurementUnit.Standard -> "K"
        MeasurementUnit.Imperial -> "°F"
        MeasurementUnit.Metric -> "°C"
    }
}

@Composable
private fun WeatherDetailRow(icon: ImageVector, label: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = icon, contentDescription = label
        )
        Text(
            text = label,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Text(
            text = value, style = MaterialTheme.typography.bodyLarge
        )

    }
}

// Previews

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    MyApplicationTheme {
        WeatherSearchScreen(weather = Weather(
            coord = Coord(lon = 0.0, lat = 0.0),
            weatherDescription = "a bit cloudy",
            temperature = 21.0,
            feelsLike = 22.0,
            cityName = "Cairo"
        ),
            onCitySearch = {},
            temperatureUnit = MeasurementUnit.Metric,
            onTemperatureUnitChange = {})
    }
}

@Preview(showBackground = true, widthDp = 480)
@Composable
private fun PortraitPreview() {
    MyApplicationTheme {
        WeatherSearchScreen(weather = Weather(
            coord = Coord(lon = 0.0, lat = 0.0),
            weatherDescription = "a bit cloudy",
            temperature = 21.0,
            feelsLike = 22.0,
            cityName = "Cairo"
        ),
            onCitySearch = {},
            temperatureUnit = MeasurementUnit.Metric,
            onTemperatureUnitChange = {})
    }
}

@Preview(showBackground = true, widthDp = 480)
@Composable
private fun LoadingPortraitPreview() {
    MyApplicationTheme {
        WeatherSearchScreen(isLoading = true,
            weather = Weather(
                coord = Coord(lon = 0.0, lat = 0.0),
                weatherDescription = "a bit cloudy",
                temperature = 21.0,
                feelsLike = 22.0,
                cityName = "Cairo"
            ),
            onCitySearch = {},
            temperatureUnit = MeasurementUnit.Metric,
            onTemperatureUnitChange = {})
    }
}

@Preview(showBackground = true, widthDp = 480)
@Composable
private fun ErrorPortraitPreview() {
    MyApplicationTheme {
        WeatherSearchScreen(error = "City not found",
            weather = Weather(
                coord = Coord(lon = 0.0, lat = 0.0),
                weatherDescription = "a bit cloudy",
                temperature = 21.0,
                feelsLike = 22.0,
                cityName = "Cairo"
            ),
            onCitySearch = {},
            temperatureUnit = MeasurementUnit.Metric,
            onTemperatureUnitChange = {})
    }
}
