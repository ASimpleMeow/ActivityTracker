package org.wit.activitytracker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import org.wit.activitytracker.R
import org.wit.activitytracker.adapters.ActivityAdapter
import org.wit.activitytracker.adapters.ActivityListener
import org.wit.activitytracker.databinding.FragmentOverviewBinding
import org.wit.activitytracker.main.MainApp
import org.wit.activitytracker.models.Activity
import org.wit.activitytracker.models.ActivityType
import timber.log.Timber
import kotlin.random.Random

class OverviewFragment : Fragment(), ActivityListener {

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
        fragBinding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_overviewFragment_to_activityTypeListFragment)
        }
        renderActivities(app.activityStore.findAll())
        val barChart = configureOverviewChart()
        barChart.setOnChartValueSelectedListener(object: OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                Timber.i("Selected $e")
                val typeIndex = (e?.x ?: 0)
                val type = ActivityType.values()[typeIndex.toInt()]
                Timber.i("Selected $e - $type")
                val activities = app.activityStore.findAll().filter { it.type == type }
                renderActivities(activities)
            }

            override fun onNothingSelected() {
                Timber.i("Selected Nothing")
                val activities = app.activityStore.findAll()
                renderActivities(activities)
            }
        })
        return fragBinding.root
    }

    private fun configureOverviewChart(): BarChart {
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
        return overviewChart
    }

    private fun populateOverviewChartData(): BarData {
        val dataSets = ArrayList<IBarDataSet>()

        ActivityType.values().forEachIndexed { index, type ->
            val amount = app.activityStore.findAll().filter { it.type == type }.count().toFloat()
            val entries = ArrayList<BarEntry>()
            entries.add(BarEntry(index.toFloat(), amount, type))
            val barDataSet = BarDataSet(entries, type.typeName)
            barDataSet.color = ContextCompat.getColor(app, type.color)
            dataSets.add(barDataSet)
        }
        return BarData(dataSets)
    }

    private fun renderActivities(activities: List<Activity>) {
        val activitiesToRender = activities.sortedByDescending { it.start }
        val layoutManager = LinearLayoutManager(requireContext())
        fragBinding.recyclerView.layoutManager = layoutManager
        fragBinding.recyclerView.adapter = ActivityAdapter(activitiesToRender, this)
    }

    override fun onActivityClick(activity: Activity) {
        val bundle = bundleOf("activity" to activity)
        this.findNavController().navigate(R.id.action_overviewFragment_to_activityDetailFragment, bundle)
    }

    override fun onResume() {
        super.onResume()
        renderActivities(app.activityStore.findAll())
        configureOverviewChart()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }
}