package org.wit.activitytracker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.wit.activitytracker.adapters.ActivityTypeAdapter
import org.wit.activitytracker.adapters.ActivityTypeListener
import org.wit.activitytracker.databinding.FragmentActivityTypeListBinding
import org.wit.activitytracker.models.ActivityType

class ActivityTypeListFragment: Fragment(), ActivityTypeListener {

    private var _fragBinding: FragmentActivityTypeListBinding? = null
    private val fragBinding get() = _fragBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _fragBinding = FragmentActivityTypeListBinding.inflate(inflater, container, false)

        val activities: List<ActivityType> = ActivityType.values().toList()
        val layoutManager = LinearLayoutManager(requireContext())
        fragBinding.recyclerView.layoutManager = layoutManager
        fragBinding.recyclerView.adapter = ActivityTypeAdapter(activities, this)

        return fragBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

    override fun onActivityTypeClick(activityType: ActivityType) {
        val bundle = bundleOf("activityType" to activityType)
        this.findNavController().navigate(activityType.resId, bundle)
    }

}