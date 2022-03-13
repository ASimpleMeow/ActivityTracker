package org.wit.activitytracker.fragments

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.PermissionRequest
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.wit.activitytracker.databinding.FragmentOverviewBinding
import timber.log.Timber.i

class OverviewFragment : Fragment() {

    private var _fragBinding: FragmentOverviewBinding? = null
    private val fragBinding get() = _fragBinding!!

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var permissionLauncher: ActivityResultLauncher<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                // Do if the permission is granted
                i("KETTLE have permission")
            }
            else {
                // Do otherwise
                i("KETTLE no have permission")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _fragBinding = FragmentOverviewBinding.inflate(inflater, container, false)

        fragBinding.getLocation.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                fusedLocationClient.lastLocation.addOnSuccessListener {
                    fragBinding.location.setText("${it?.latitude}, ${it?.longitude}")
                }
            } else {
                permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }

        return fragBinding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            OverviewFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}