package org.wit.activitytracker.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import org.wit.activitytracker.R
import org.wit.activitytracker.databinding.FragmentCounterActivityBinding
import org.wit.activitytracker.main.MainApp
import org.wit.activitytracker.models.Activity
import org.wit.activitytracker.models.ActivityType
import org.wit.activitytracker.models.map.Path
import timber.log.Timber
import java.util.*

class CounterActivityFragment : Fragment() {

    private var _fragBinding: FragmentCounterActivityBinding? = null
    private val fragBinding get() = _fragBinding!!

    private lateinit var app: MainApp
    private lateinit var activityType: ActivityType

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityType = arguments?.getSerializable("activityType") as ActivityType
        app = activity?.application as MainApp
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _fragBinding = FragmentCounterActivityBinding.inflate(inflater, container, false)

        fragBinding.title.text = activityType.typeName
        fragBinding.description.text = activityType.description
        fragBinding.counter.minValue = 0
        fragBinding.counter.maxValue = 9999
        val start = Date()

        fragBinding.finish.setOnClickListener {
            Timber.i("${fragBinding.counter.value}")
            val activity = Activity()
            activity.apply {
                this.start = start
                this.stop = Date()
                this.type = activityType
                this.count = fragBinding.counter.value
            }
            app.activityStore.create(activity)
            this.findNavController().navigate(R.id.action_counterActivityFragment_to_overviewFragment)
        }

        return fragBinding.root
    }
}