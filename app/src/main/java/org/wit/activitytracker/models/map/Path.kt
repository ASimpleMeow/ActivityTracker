package org.wit.activitytracker.models.map

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Path(var points: List<Point> = ArrayList()): Parcelable
