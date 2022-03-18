package org.wit.activitytracker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.wit.activitytracker.adapters.ActivityTypeAdapter
import org.wit.activitytracker.adapters.ActivityTypeListener
import org.wit.activitytracker.databinding.FragmentOverviewBinding
import org.wit.activitytracker.models.ActivityType

class OverviewFragment : Fragment(), ActivityTypeListener {

    private var _fragBinding: FragmentOverviewBinding? = null
    private val fragBinding get() = _fragBinding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _fragBinding = FragmentOverviewBinding.inflate(inflater, container, false)
        val activities: List<ActivityType> = ActivityType.values().toList()
        val layoutManager = LinearLayoutManager(requireContext())
        fragBinding.recyclerView.layoutManager = layoutManager
        fragBinding.recyclerView.adapter = ActivityTypeAdapter(activities, this)

        return fragBinding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            OverviewFragment().apply {
                arguments = Bundle().apply {}
            }
    }

    override fun onActivityTypeClick(activityType: ActivityType) {
        this.findNavController().navigate(activityType.resId)
    }
}