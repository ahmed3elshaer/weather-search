package com.ahmed3elshaer.weather.data.storage.di

import android.content.Context
import android.content.SharedPreferences
import com.ahmed3elshaer.weather.data.storage.UnitPreference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StorageModule {
	@Provides
	@Singleton
	fun provideSharedPreferences(@ApplicationContext appContext: Context): SharedPreferences {
		return appContext.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
	}

	@Provides
	@Singleton
	fun provideUnitPreference(sharedPreferences: SharedPreferences): UnitPreference {
		return UnitPreference(sharedPreferences)
	}
}

