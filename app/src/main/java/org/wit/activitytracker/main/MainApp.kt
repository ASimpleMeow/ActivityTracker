package org.wit.activitytracker.main

import android.app.Application
import org.wit.activitytracker.models.ActivityStore
import org.wit.activitytracker.models.JSONActivityStore

class MainApp: Application() {

    lateinit var activityStore : ActivityStore

    override fun onCreate() {
        super.onCreate()
        activityStore = JSONActivityStore(applicationContext)
    }
}