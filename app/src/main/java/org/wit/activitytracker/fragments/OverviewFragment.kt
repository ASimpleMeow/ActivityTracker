package org.wit.activitytracker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.wit.activitytracker.activities.Home
import org.wit.activitytracker.adapters.ActivityTypeAdapter
import org.wit.activitytracker.adapters.ActivityTypeListener
import org.wit.activitytracker.databinding.FragmentOverviewBinding
import org.wit.activitytracker.models.ActivityType

class OverviewFragment : Fragment() {

    private var _fragBinding: FragmentOverviewBinding? = null
    private val fragBinding get() = _fragBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _fragBinding = FragmentOverviewBinding.inflate(inflater, container, false)
        return fragBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }
}