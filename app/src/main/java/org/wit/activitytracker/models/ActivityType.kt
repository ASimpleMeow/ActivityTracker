package org.wit.activitytracker.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class ActivityType : Parcelable {
    WALKING {
        override fun useMap() = true
    },
    RUNNING {
        override fun useMap() = true
    },
    SQUATS {
        override fun useMap() = false
    },
    PUSHUPS {
        override fun useMap() = false
    },
    SITUPS {
        override fun useMap() = false
    };

    abstract fun useMap(): Boolean
}