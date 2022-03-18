package org.wit.activitytracker.models.map

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Point(
    var lat: Double = 0.0,
    var lng: Double = 0.0,
    var altitude: Double = 0.0,
    var timestamp: Date = Date()
): Parcelable
