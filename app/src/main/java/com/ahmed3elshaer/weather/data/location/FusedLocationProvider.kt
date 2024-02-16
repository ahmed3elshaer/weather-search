package com.ahmed3elshaer.weather.data.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface LocationProvider {
	suspend fun getCurrentLocation(): LocationData
}

data class LocationData(val latitude: Double, val longitude: Double)

class FusedLocationProvider @Inject constructor(private val context: Context) : LocationProvider {

	private val fusedLocationClient: FusedLocationProviderClient =
		LocationServices.getFusedLocationProviderClient(context)

	override suspend fun getCurrentLocation(): LocationData {
		return if (ContextCompat.checkSelfPermission(
				context,
				Manifest.permission.ACCESS_FINE_LOCATION
			) == PackageManager.PERMISSION_GRANTED
		) {
			val lastLocation = fusedLocationClient.getCurrentLocation(
				Priority.PRIORITY_HIGH_ACCURACY,
				null
			).await()
			LocationData(lastLocation.latitude, lastLocation.longitude)
		} else {
			throw IllegalStateException("Cannot get the weather by Location, Location Permission is required.")
		}
	}
}