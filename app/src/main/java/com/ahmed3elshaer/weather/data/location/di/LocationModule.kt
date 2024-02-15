package com.ahmed3elshaer.weather.data.location.di

import android.content.Context
import com.ahmed3elshaer.weather.data.location.FusedLocationProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocationModule {
    @Provides
    @Singleton
    fun provideFusedLocation(@ApplicationContext appContext: Context): FusedLocationProvider {
        return FusedLocationProvider(appContext)
    }
}