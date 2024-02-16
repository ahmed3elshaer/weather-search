package com.ahmed3elshaer.weather.data.storage

import android.content.SharedPreferences
import com.ahmed3elshaer.weather.data.storage.model.MeasurementUnit
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.startWith
import javax.inject.Inject

class UnitPreference @Inject constructor(private val sharedPreferences: SharedPreferences) {
    companion object {
        private const val UNIT_KEY = "unit_key"
    }

    fun saveUnit(unit: MeasurementUnit) {
        sharedPreferences.edit().putString(UNIT_KEY, unit.name).apply()
    }

    fun getSingleUnit(): MeasurementUnit {
        return MeasurementUnit.valueOf(
            sharedPreferences.getString(
                UNIT_KEY, MeasurementUnit.Metric.name
            )!!
        )
    }

    fun getUnit(): Flow<MeasurementUnit> {
        return callbackFlow {
            val listener =
                SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
                    if (key == UNIT_KEY) {
                        this@callbackFlow.trySend(
                            MeasurementUnit.valueOf(
                                sharedPreferences.getString(
                                    UNIT_KEY, MeasurementUnit.Metric.name
                                )!!
                            )
                        )
                    }
                }
            sharedPreferences.registerOnSharedPreferenceChangeListener(
                listener
            )
            awaitClose {
                sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
            }
        }.onStart {
            emit(
                MeasurementUnit.valueOf(
                    sharedPreferences.getString(
                        UNIT_KEY, MeasurementUnit.Metric.name
                    )!!
                )
            )
        }
    }
}