package org.wit.activitytracker.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import org.wit.activitytracker.R
import org.wit.activitytracker.databinding.HomeBinding
import org.wit.activitytracker.models.ActivityStore
import org.wit.activitytracker.models.JSONActivityStore
import timber.log.Timber

class Home : AppCompatActivity() {

    private lateinit var homeBinding : HomeBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(Timber.DebugTree())
        Timber.i("ActivityTracker started")
        homeBinding = HomeBinding.inflate(layoutInflater)
        drawerLayout = homeBinding.drawerLayout
        navView = homeBinding.navView
        setContentView(homeBinding.root)

        val materialToolbar = homeBinding.topActionBar
        setSupportActionBar(materialToolbar)

        appBarConfiguration = AppBarConfiguration(setOf(R.id.overviewFragment), drawerLayout)

        val navController = findNavController(R.id.nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
        NavigationUI.setupWithNavController(navView, navController)

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp()
    }
}