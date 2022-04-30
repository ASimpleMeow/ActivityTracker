package org.wit.activitytracker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import org.wit.activitytracker.activities.Home
import org.wit.activitytracker.adapters.ActivityTypeAdapter
import org.wit.activitytracker.adapters.ActivityTypeListener
import org.wit.activitytracker.databinding.FragmentOverviewBinding
import org.wit.activitytracker.main.MainApp
import org.wit.activitytracker.models.Activity
import org.wit.activitytracker.models.ActivityType
import kotlin.random.Random

class OverviewFragment : Fragment() {

    private lateinit var app: MainApp
    private var _fragBinding: FragmentOverviewBinding? = null
    private val fragBinding get() = _fragBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as MainApp
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _fragBinding = FragmentOverviewBinding.inflate(inflater, container, false)
        configureOverviewChart()
        return fragBinding.root
    }

    private fun configureOverviewChart() {
        val overviewChart = fragBinding.overviewChart
        overviewChart.setTouchEnabled(true)
        overviewChart.setPinchZoom(true)
        overviewChart.setDrawBorders(false)
        overviewChart.setDrawBarShadow(false)
        overviewChart.setDrawGridBackground(false)
        val desc = Description()
        desc.isEnabled = false
        overviewChart.description = desc
        val xAxis = overviewChart.xAxis
        xAxis.granularity = 1f
        xAxis.setDrawAxisLine(false)
        xAxis.setDrawGridLines(false)
        overviewChart.axisLeft.axisMinimum = 0f
        overviewChart.axisRight.axisMinimum = 0f
        overviewChart.axisLeft.granularity = 1f
        overviewChart.axisRight.granularity = 1f
        overviewChart.axisRight.setDrawAxisLine(false)
        overviewChart.axisRight.setDrawGridLines(false)
        overviewChart.axisLeft.setDrawAxisLine(false)

        overviewChart.data = populateOverviewChartData()
    }

    private fun populateOverviewChartData(): BarData {
        val dataSets = ArrayList<IBarDataSet>()

        val activities = app.activityStore.findAll()
        ActivityType.values().forEachIndexed { index, type ->
            val amount = activities.filter { it.type == type }.count().toFloat()
            val entries = ArrayList<BarEntry>()
            entries.add(BarEntry(index.toFloat(), amount))
            val barDataSet = BarDataSet(entries, type.typeName)
            barDataSet.color = ContextCompat.getColor(app, type.color)
            dataSets.add(barDataSet)
        }
        return BarData(dataSets)
    }

    override fun onResume() {
        super.onResume()
        configureOverviewChart()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }
}