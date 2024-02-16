import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.ahmed3elshaer.weather.data.storage.model.MeasurementUnit
import com.ahmed3elshaer.weather.domain.GetWeatherByCityUseCase
import com.ahmed3elshaer.weather.domain.GetWeatherByLocation
import com.ahmed3elshaer.weather.domain.MeasurementUseCase
import com.ahmed3elshaer.weather.domain.model.Coord
import com.ahmed3elshaer.weather.domain.model.Weather
import com.ahmed3elshaer.weather.ui.weathersearch.WeatherSearchUserEvent
import com.ahmed3elshaer.weather.ui.weathersearch.WeatherSearchViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class WeatherSearchViewModelTest {

    private lateinit var viewModel: WeatherSearchViewModel
    private lateinit var getWeatherByCityUseCase: GetWeatherByCityUseCase
    private lateinit var getWeatherByLocation: GetWeatherByLocation
    private lateinit var measurementUseCase: MeasurementUseCase

    @OptIn(ExperimentalCoroutinesApi::class)
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")


    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
        getWeatherByCityUseCase = mockk()
        getWeatherByLocation = mockk()
        measurementUseCase = mockk()

        coEvery { measurementUseCase.get() } returns MutableStateFlow(MeasurementUnit.Metric)
        viewModel = WeatherSearchViewModel(
            getWeatherByCityUseCase,
            getWeatherByLocation,
            measurementUseCase
        )
    }

    @Test
    fun `onUserEvent SearchByCity success`() = runBlocking {
        val cityName = "London"
        val expectedWeather = Weather(
            coord = Coord(lon = 0.0, lat = 0.0),
            weatherDescription = "",
            temperature = 0.0,
            feelsLike = 0.0,
            cityName = cityName
        )
        coEvery { getWeatherByCityUseCase(any()) } returns Result.success(expectedWeather)

        viewModel.onUserEvent(WeatherSearchUserEvent.SearchByCity(cityName))

        viewModel.uiState.test {
            val first = awaitItem()
            assertEquals(null, first.weather)
            assertEquals(false, first.isLoading)
            assertEquals(null, first.error)
            val state = awaitItem()
            assertEquals(expectedWeather, state.weather)
            assertEquals(false, state.isLoading)
            assertEquals(null, state.error)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onUserEvent SearchByLocation success`() = runBlocking {
        val expectedWeather = Weather(
            coord = Coord(lon = 0.0, lat = 0.0),
            weatherDescription = "",
            temperature = 0.0,
            feelsLike = 0.0,
            cityName = ""
        )

        coEvery { getWeatherByLocation() } returns Result.success(expectedWeather)

        viewModel.onUserEvent(WeatherSearchUserEvent.SearchByLocation)

        viewModel.uiState.test {
            val first = awaitItem()
            assertEquals(null, first.weather)
            assertEquals(false, first.isLoading)
            assertEquals(null, first.error)
            val state = awaitItem()
            assertEquals(expectedWeather, state.weather)
            assertEquals(false, state.isLoading)
            assertEquals(null, state.error)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onUserEvent SearchByCity failure`() = runBlocking {
        val cityName = "London"
        val errorMessage = "City not found"
        coEvery { getWeatherByCityUseCase(any()) } returns Result.failure(Exception(errorMessage))

        viewModel.onUserEvent(WeatherSearchUserEvent.SearchByCity(cityName))

        viewModel.uiState.test {
            val first = awaitItem()
            assertEquals(null, first.weather)
            assertEquals(false, first.isLoading)
            assertEquals(null, first.error)
            val state = awaitItem()
            assertEquals(null, state.weather)
            assertEquals(false, state.isLoading)
            assertEquals(errorMessage, state.error)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onUserEvent SearchByLocation failure`() = runBlocking {
        val errorMessage = "Location not available"
        coEvery { getWeatherByLocation() } returns Result.failure(Exception(errorMessage))

        viewModel.onUserEvent(WeatherSearchUserEvent.SearchByLocation)

        viewModel.uiState.test {
            val first = awaitItem()
            assertEquals(null, first.weather)
            assertEquals(false, first.isLoading)
            assertEquals(null, first.error)
            val state = awaitItem()
            assertEquals(null, state.weather)
            assertEquals(false, state.isLoading)
            assertEquals(errorMessage, state.error)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun changeMeasurementUnit() = runBlocking {
        val expectedUnit = MeasurementUnit.Metric
        coEvery { measurementUseCase.change(any()) } returns Unit

        viewModel.changeMeasurementUnit(expectedUnit)

        viewModel.measurementUnit.test {
            assertEquals(expectedUnit, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
        // ... rest of tear down
    }
}
