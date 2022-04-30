package org.wit.activitytracker.fragments

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.IBinder
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.wit.activitytracker.R
import org.wit.activitytracker.databinding.FragmentGpsActivityBinding
import org.wit.activitytracker.main.MainApp
import org.wit.activitytracker.models.Activity
import org.wit.activitytracker.models.ActivityType
import org.wit.activitytracker.models.map.Path
import org.wit.activitytracker.services.LocationService
import timber.log.Timber

class GpsActivityFragment : Fragment() {

    private var _fragBinding: FragmentGpsActivityBinding? = null
    private val fragBinding get() = _fragBinding!!

    private lateinit var app: MainApp
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var locationService: LocationService
    private lateinit var activityType: ActivityType

    private val serviceConnection = object: ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, iBinder: IBinder?) {
            val localBinder: LocationService.LocalBinder = iBinder as LocationService.LocalBinder
            locationService = localBinder.getBindServiceInstance()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as MainApp
        activityType = arguments?.getSerializable("activityType") as ActivityType
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                // Do if the permission is granted
                Timber.i("GPS has permission")
            }
            else {
                // Do otherwise
                Timber.i("GPS doesn't have permission")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _fragBinding = FragmentGpsActivityBinding.inflate(inflater, container, false)
        val intent = Intent(requireContext(), LocationService::class.java)
        requireContext().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)

        fragBinding.startLocation.setOnClickListener {
            locationService.startLocationService()
        }

        fragBinding.finish.setOnClickListener {
            val points = locationService.getLocations()
            val startDate = locationService.getStartDate()
            val endDate = locationService.getEndDate()
            locationService.stopLocationService()

            points.forEach {
                Timber.i("${it.timestamp} : ${it.lat}, ${it.lng}, ${it.altitude}")
            }
            val activity = Activity()
            activity.apply {
                this.start = startDate
                this.stop = endDate
                this.type = activityType
                this.path = Path(points)
            }
            app.activityStore.create(activity)
            this.findNavController().navigate(R.id.action_gpsActivityFragment_to_overviewFragment)
        }

        fragBinding.title.text = activityType.typeName
        fragBinding.description.text = activityType.description

        if (ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        return fragBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
        requireContext().unbindService(serviceConnection)
    }
}