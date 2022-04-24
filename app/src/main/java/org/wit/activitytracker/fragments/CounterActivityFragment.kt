package org.wit.activitytracker.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.wit.activitytracker.databinding.FragmentCounterActivityBinding
import org.wit.activitytracker.models.ActivityType
import timber.log.Timber

class CounterActivityFragment : Fragment() {

    private var _fragBinding: FragmentCounterActivityBinding? = null
    private val fragBinding get() = _fragBinding!!

    private lateinit var activityType: ActivityType

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityType = arguments?.getSerializable("activityType") as ActivityType
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _fragBinding = FragmentCounterActivityBinding.inflate(inflater, container, false)

        fragBinding.title.text = activityType.typeName
        fragBinding.counter.minValue = 0

        fragBinding.finish.setOnClickListener {
            Timber.i("${fragBinding.counter.value}")
        }

        return fragBinding.root
    }
}