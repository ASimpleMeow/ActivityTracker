package org.wit.activitytracker.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.wit.activitytracker.models.map.Path
import java.util.*

@Parcelize
data class Activity(
    var id: Long = 0L,
    var start: Date? = Date(),
    var stop: Date? = Date(),
    var type: ActivityType = ActivityType.WALKING,
    var path: Path = Path(),
    var count: Int = 0,
    var comment: String = "",
): Parcelable
