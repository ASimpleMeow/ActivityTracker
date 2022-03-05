package org.wit.activitytracker.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.wit.activitytracker.models.map.Path
import java.util.*

@Parcelize
data class Activity(
    var start: Date = Date(),
    var stop: Date = Date(),
    var type: ActivityType = ActivityType.WALKING,
    var path: Path = Path(),
    var comment: String = "",
): Parcelable
