package com.ahmed3elshaer.weather.data.storage

import android.content.SharedPreferences
import com.ahmed3elshaer.weather.data.storage.model.MeasurementUnit
import javax.inject.Inject

class UnitPreference @Inject constructor(private val sharedPreferences: SharedPreferences) {
	companion object {
		private const val UNIT_KEY = "unit_key"
	}

	fun saveUnit(unit: MeasurementUnit) {
		sharedPreferences.edit().putString(UNIT_KEY, unit.name).apply()
	}

	fun getUnit(): MeasurementUnit {
		val unitString = sharedPreferences.getString(UNIT_KEY, MeasurementUnit.CELSIUS.name)
		return unitString?.let { MeasurementUnit.valueOf(it) } ?: MeasurementUnit.CELSIUS
	}
}