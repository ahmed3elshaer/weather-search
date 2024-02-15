package com.ahmed3elshaer.weather.ui.weathersearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmed3elshaer.weather.data.storage.model.MeasurementUnit
import com.ahmed3elshaer.weather.domain.GetWeatherByCityUseCase
import com.ahmed3elshaer.weather.domain.GetWeatherByLocation
import com.ahmed3elshaer.weather.domain.MeasurementUseCase
import com.ahmed3elshaer.weather.domain.model.Weather
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherSearchViewModel @Inject constructor(
    private val getWeatherByCityUseCase: GetWeatherByCityUseCase,
    private val getWeatherByLocation: GetWeatherByLocation,
    private val measurementUseCase: MeasurementUseCase

) : ViewModel() {
    private var lastEvent: WeatherSearchUserEvent = WeatherSearchUserEvent.SearchByLocation

    private val uiStateImpl = MutableStateFlow(WeatherSearchUiState())

    val uiState: StateFlow<WeatherSearchUiState> = uiStateImpl
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = WeatherSearchUiState()
        )

    init {
        onUserEvent(lastEvent)
    }

    fun onUserEvent(event: WeatherSearchUserEvent) {
        viewModelScope.launch {
            uiStateImpl.value = uiState.value.copy(isLoading = true)
            val result = when (event) {
                is WeatherSearchUserEvent.SearchByCity -> getWeatherByCityUseCase(event.name)
                WeatherSearchUserEvent.SearchByLocation -> getWeatherByLocation()
            }
            result.fold(
                onSuccess = { weather ->
                    uiStateImpl.value = uiState.value.copy(weather = weather)
                },
                onFailure = { error ->
                    uiStateImpl.value = uiState.value.copy(error = error.message)
                }
            )
        }
    }

    fun changeMeasurementUnit(unit: MeasurementUnit) {
        viewModelScope.launch {
            measurementUseCase.change(unit)
            onUserEvent(lastEvent)
        }
    }

}

sealed interface WeatherSearchUserEvent {
    data class SearchByCity(val name: String) : WeatherSearchUserEvent
    data object SearchByLocation : WeatherSearchUserEvent
}

data class WeatherSearchUiState(
    val weather: Weather? = null,
    val error: String? = null,
    val isLoading: Boolean = false,
    val unit: MeasurementUnit = MeasurementUnit.CELSIUS
)
