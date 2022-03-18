package org.wit.activitytracker.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import org.wit.activitytracker.R
import org.wit.activitytracker.databinding.HomeBinding
import timber.log.Timber

class Home : AppCompatActivity() {

    private lateinit var homeBinding : HomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(Timber.DebugTree())
        Timber.i("ActivityTracker started")
        homeBinding = HomeBinding.inflate(layoutInflater)
        setContentView(homeBinding.root)

        val navController = findNavController(R.id.nav_host_fragment)
        val navView = homeBinding.navView
        navView.setupWithNavController(navController)
    }
}