package org.wit.activitytracker.services

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Binder
import android.os.IBinder
import android.os.Looper
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import org.wit.activitytracker.R
import org.wit.activitytracker.models.map.Point
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList

const val LOCATION_SERVICE_ID = 175

class LocationService : Service() {

    private val locations: MutableList<Point> = ArrayList()
    private val localBinder = LocalBinder()
    private val locationCallback = object: LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            val lat = locationResult.lastLocation.latitude
            val lng = locationResult.lastLocation.longitude
            val alt = locationResult.lastLocation.altitude
            val timestamp = locationResult.lastLocation.time
            val point = Point(lat, lng, alt, Date(timestamp))
            locations.add(point)
        }
    }

    override fun onBind(intent: Intent): IBinder {
        return localBinder
    }

    fun getLocations(): List<Point> {
        return ArrayList(locations)
    }

    fun startLocationService() {
        Timber.i("Location Service Starting")
        locations.clear()
        val channelId = "location_notification_channel"
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val resultIntent = Intent()
        val pendingIntent = PendingIntent.getActivity(applicationContext,
            0,
            resultIntent,
            PendingIntent.FLAG_UPDATE_CURRENT)
        val builder = NotificationCompat.Builder(applicationContext, channelId)
        builder.setSmallIcon(R.mipmap.ic_launcher)
        builder.setContentTitle("Location Service")
        builder.setDefaults(NotificationCompat.DEFAULT_ALL)
        builder.setContentText("Running")
        builder.setContentIntent(pendingIntent)
        builder.setAutoCancel(false)
        builder.priority = NotificationCompat.PRIORITY_MAX

        if (notificationManager.getNotificationChannel(channelId) == null) {
            val notificationChannel = NotificationChannel(channelId, "Location Service", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val locationRequest = LocationRequest.create().apply {
            interval = 4000
            fastestInterval = 2000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Timber.e("Location Permission Not Granted")
            Toast.makeText(this, "", Toast.LENGTH_LONG).show()
        }

        LocationServices.getFusedLocationProviderClient(this)
            .requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        startForeground(LOCATION_SERVICE_ID, builder.build())
    }

    fun stopLocationService() {
        Timber.i("Location Service Stopping")
        locations.clear()
        LocationServices.getFusedLocationProviderClient(this).removeLocationUpdates(locationCallback)
        stopForeground(true)
        stopSelf()
    }

    inner class LocalBinder: Binder() {

        fun getBindServiceInstance(): LocationService {
            return this@LocationService
        }
    }
}