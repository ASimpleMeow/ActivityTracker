package org.wit.activitytracker.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.view.*
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import org.wit.activitytracker.R
import org.wit.activitytracker.databinding.FragmentActivityDetailBinding
import org.wit.activitytracker.main.MainApp
import org.wit.activitytracker.models.Activity
import timber.log.Timber
import java.util.*

class ActivityDetailFragment: Fragment(), OnMapReadyCallback, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private lateinit var app: MainApp
    private lateinit var activityModel: Activity

    private var _fragBinding: FragmentActivityDetailBinding? = null
    private val fragBinding get() = _fragBinding!!

    private var updatingStartDate = false
    private var updatingEndDate = false

    private var newYear = 0
    private var newMonth = 0
    private var newDay = 0
    private var newHour = 0
    private var newMinute = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as MainApp
        activityModel = arguments?.getParcelable("activity")!!
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _fragBinding = FragmentActivityDetailBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        fragBinding.counter.minValue = 0
        fragBinding.counter.maxValue = 9999
        render()
        val supportMapFrag = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        if (!activityModel.type.useMap()) {
            supportMapFrag.view?.visibility = View.INVISIBLE
            fragBinding.counter.setOnValueChangedListener { _, _, newVal ->
                activityModel.count = newVal
            }
        } else {
            fragBinding.counter.visibility = View.INVISIBLE
            fragBinding.count.visibility = View.INVISIBLE
            supportMapFrag.getMapAsync(this)
        }
        fragBinding.startDateBtn.setOnClickListener {
            updatingStartDate = true
            updatingEndDate = false
            showDateTimePicker()
        }
        fragBinding.endDateBtn.setOnClickListener {
            updatingStartDate = false
            updatingEndDate = true
            showDateTimePicker()
        }
        return fragBinding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_activity, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.item_cancel -> findNavController().navigate(R.id.action_activityDetailFragment_to_overviewFragment)
            R.id.item_delete -> {
                app.activityStore.delete(activityModel.id)
                findNavController().navigate(R.id.action_activityDetailFragment_to_overviewFragment)
            }
            R.id.item_save -> {
                app.activityStore.update(activityModel)
                findNavController().navigate(R.id.action_activityDetailFragment_to_overviewFragment)
            }
        }
        return NavigationUI.onNavDestinationSelected(item, findNavController())
                || super.onOptionsItemSelected(item)
    }

    private fun render() {
        fragBinding.title.text = activityModel.type.typeName
        fragBinding.startDateBtn.text = activityModel.start.toString()
        fragBinding.endDateBtn.text = activityModel.stop.toString()
        fragBinding.counter.value = activityModel.count
    }

    private fun showDateTimePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR);
        val month = calendar.get(Calendar.MONTH);
        val day = calendar.get(Calendar.DAY_OF_MONTH);
        val datePicker = DatePickerDialog(requireContext(), this, year, month, day)
        datePicker.show()
    }

    override fun onMapReady(map: GoogleMap) {
        if (activityModel.path.points.isEmpty()) {
            Timber.i("No path to plot")
            return
        }
        val polyLine = PolylineOptions()
        polyLine.clickable(false)
        polyLine.width(10f)
        polyLine.color(R.color.blue)
        activityModel.path.points.forEach { p ->
            polyLine.add(LatLng(p.lat, p.lng))
        }
        map.addPolyline(polyLine)
        if (activityModel.path.points.isNotEmpty()) {
            val startPoint = activityModel.path.points.first()
            val endPoint = activityModel.path.points.last()
            val markerOptionsStart = MarkerOptions()
            markerOptionsStart.title("Start")
            markerOptionsStart.position(LatLng(startPoint.lat, startPoint.lng))
            val markerOptionsEnd = MarkerOptions()
            markerOptionsEnd.title("End")
            markerOptionsEnd.position(LatLng(endPoint.lat, endPoint.lng))
            map.addMarker(markerOptionsStart)
            map.addMarker(markerOptionsEnd)
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(startPoint.lat, startPoint.lng), 15f))
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        newYear = year
        newMonth = month
        newDay = dayOfMonth
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR)
        val minute = calendar.get(Calendar.MINUTE)
        val timePickerDialog = TimePickerDialog(requireContext(), this, hour, minute, DateFormat.is24HourFormat(requireContext()))
        timePickerDialog.show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        newHour = hourOfDay
        newMinute = minute
        val cal = Calendar.getInstance()
        cal.set(newYear, newMonth, newDay, newHour, newMinute)
        val newDate = Date(cal.timeInMillis)
        if (updatingStartDate) {
            activityModel.start = newDate
        } else if(updatingEndDate) {
            activityModel.stop = newDate
        }
        Timber.i("${activityModel.start} - ${activityModel.stop}")
        render()
    }
}