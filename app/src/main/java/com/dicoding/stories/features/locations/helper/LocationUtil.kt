package com.dicoding.stories.features.locations.helper

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.LocationManager
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import kotlinx.io.IOException
import java.util.concurrent.TimeUnit

@Suppress("DEPRECATION")
object LocationUtil {
  private const val LOCATION_TAG = "LocationUtil"

  fun stopLocationUpdate(
    provider: FusedLocationProviderClient,
    callback: LocationCallback,
  ) {
    try {
      val removeTask = provider.removeLocationUpdates(callback)
      removeTask.addOnCompleteListener { task ->
        if (task.isSuccessful) {
          Log.d(LOCATION_TAG, "Location Callback removed.")
        } else {
          Log.d(LOCATION_TAG, "Failed to remove Location Callback.")
        }
      }
    } catch (se: SecurityException) {
      Log.e(LOCATION_TAG, "Failed to remove Location Callback.. $se")
    }
  }

  @SuppressLint("MissingPermission")
  fun locationUpdate(
    provider: FusedLocationProviderClient,
    callback: LocationCallback,
  ) {
    callback.let {
      val locationRequest: LocationRequest = LocationRequest.create().apply {
        interval = TimeUnit.SECONDS.toMillis(60)
        fastestInterval = TimeUnit.SECONDS.toMillis(30)
        maxWaitTime = TimeUnit.MINUTES.toMillis(2)
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
      }

      provider.requestLocationUpdates(
        locationRequest,
        it,
        Looper.getMainLooper()
      )
    }
  }

  fun getReadableLocation(
    context: Context,
    latitude: Double,
    longitude: Double,
  ): String? {
    var addressText: String? = ""
    val geocoder = Geocoder(context, context.resources.configuration.locales[0])

    try {
      val addresses = geocoder.getFromLocation(latitude, longitude, 1)

      if (addresses?.isNotEmpty() == true) {
        val address = addresses[0]
        addressText = "${address.getAddressLine(0)}, ${address.locality}"
        Log.d("geolocation", addressText)
      }
    } catch (e: IOException) {
      Log.d("geolocation", e.message.toString())
      addressText = null
    }

    return addressText
  }

  fun locationServiceIsActive(context: Context): Boolean {
    val locationService =
      context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    val gpsEnabled =
      locationService.isProviderEnabled(LocationManager.GPS_PROVIDER)
    val networkEnabled =
      locationService.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

    return gpsEnabled && networkEnabled
  }
}
