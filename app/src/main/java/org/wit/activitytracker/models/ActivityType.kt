package org.wit.activitytracker.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.wit.activitytracker.R

@Parcelize
enum class ActivityType(val typeName: String, val description: String, val resId: Int) : Parcelable {
    WALKING ("Walking", "Low to Medium intensity activity", R.id.gpsActivityFragment) {
        override fun useMap() = true
    },
    RUNNING ("Running", "High intensity activity", R.id.gpsActivityFragment){
        override fun useMap() = true
    },
    SQUATS ("Squats", "Low to Medium intensity activity", R.id.gpsActivityFragment){
        override fun useMap() = false
    },
    PUSHUPS ("Push-ups", "Medium intensity activity", R.id.gpsActivityFragment){
        override fun useMap() = false
    },
    SITUPS ("Sit-ups", "Medium intensity activity", R.id.gpsActivityFragment){
        override fun useMap() = false
    };

    abstract fun useMap(): Boolean
}