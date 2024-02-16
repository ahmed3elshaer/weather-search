package com.ahmed3elshaer.weather.testdi

import com.ahmed3elshaer.weather.data.DefaultWeatherSearchRepository
import com.ahmed3elshaer.weather.data.FakeWeatherRepository
import com.ahmed3elshaer.weather.data.WeatherSearchRepository
import com.ahmed3elshaer.weather.data.di.DataModule
import com.ahmed3elshaer.weather.data.location.FusedLocationProvider
import com.ahmed3elshaer.weather.data.location.LocationProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataModule::class]
)
interface FakeDataModule {
    @Singleton
    @Binds
    fun bindsWeatherSearchRepository(
        weatherSearchRepository: FakeWeatherRepository
    ): WeatherSearchRepository

    @Singleton
    @Binds
    fun bindsLocationProvider(
        fusedLocationProvider: FusedLocationProvider
    ): LocationProvider

}