package com.ahmed3elshaer.weather.ui.weathersearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmed3elshaer.weather.data.storage.model.MeasurementUnit
import com.ahmed3elshaer.weather.domain.GetWeatherByCityUseCase
import com.ahmed3elshaer.weather.domain.GetWeatherByLocation
import com.ahmed3elshaer.weather.domain.MeasurementUseCase
import com.ahmed3elshaer.weather.domain.ValidateSearchInputUseCase
import com.ahmed3elshaer.weather.domain.exceptions.CityNotFound
import com.ahmed3elshaer.weather.domain.exceptions.IncorrectCityName
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
    private val measurementUseCase: MeasurementUseCase,
    private val validateSearchInputUseCase: ValidateSearchInputUseCase

) : ViewModel() {
    private var lastEvent: WeatherSearchUserEvent = WeatherSearchUserEvent.SearchByLocation

    private val uiStateImpl = MutableStateFlow(WeatherSearchUiState())

    val measurementUnit: StateFlow<MeasurementUnit> = measurementUseCase.get().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = MeasurementUnit.Metric
        )

    val uiState: StateFlow<WeatherSearchUiState> = uiStateImpl.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = WeatherSearchUiState()
        )

    fun onUserEvent(event: WeatherSearchUserEvent) {
        if (uiState.value.isLoading) {
            return
        }
        lastEvent = event
        setLoadingState(true)
        viewModelScope.launch {
            val result: Result<Weather> = when (event) {
                is WeatherSearchUserEvent.SearchByCity -> handleSearchByCityEvent(event)
                WeatherSearchUserEvent.SearchByLocation -> getWeatherByLocation()
            }
            handleResult(result)
        }
    }

    private suspend fun handleSearchByCityEvent(event: WeatherSearchUserEvent.SearchByCity): Result<Weather> {
        val validation = validateSearchInputUseCase(event.name)
        return validation.fold(onSuccess = { getWeatherByCityUseCase(event.name) },
            onFailure = { error -> Result.failure(error) })
    }

    private fun handleResult(result: Result<Weather>) {
        result.fold(onSuccess = { weather ->
            setSuccessfulState(weather)
        }, onFailure = { error ->
            setErrorState(error)
        })
        setLoadingState(false)
    }

    private fun setLoadingState(isLoading: Boolean) {
        uiStateImpl.value = uiStateImpl.value.copy(isLoading = isLoading)
    }

    private fun setSuccessfulState(weather: Weather) {
        uiStateImpl.value = uiStateImpl.value.copy(
            weather = weather,
            isLoading = false,
            error = null,
            isEmpty = false,
            inputFieldError = null
        )
    }

    private fun setErrorState(error: Throwable) {
        when (error) {
            is IncorrectCityName -> uiStateImpl.value = setError(error.message)
            is CityNotFound -> setCityNotFoundState()
            else -> setGenericErrorState(error)
        }
    }

    private fun setError(msg: String?) = uiStateImpl.value.copy(
        inputFieldError = msg,
        error = null,
        isEmpty = false,
        isLoading = false,
    )

    private fun setCityNotFoundState() {
        uiStateImpl.value = uiStateImpl.value.copy(
            error = null,
            isEmpty = true,
            inputFieldError = null,
            isLoading = false,
            weather = null
        )
    }

    private fun setGenericErrorState(error: Throwable) {
        uiStateImpl.value = uiStateImpl.value.copy(
            error = error.message,
            isLoading = false,
            inputFieldError = null,
            isEmpty = false
        )
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
    val inputFieldError: String? = null,
    val isEmpty: Boolean = false,
    val isLoading: Boolean = false,
    val unit: MeasurementUnit = MeasurementUnit.Metric
)