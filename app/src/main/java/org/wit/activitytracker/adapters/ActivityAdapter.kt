package org.wit.activitytracker.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.wit.activitytracker.databinding.CardActivityBinding
import org.wit.activitytracker.models.Activity

interface ActivityListener {
    fun onActivityClick(activity: Activity)
}

class ActivityAdapter constructor(private val activities: List<Activity>,
                                  private val listener: ActivityListener): RecyclerView.Adapter<ActivityAdapter.MainHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityAdapter.MainHolder {
        val binding = CardActivityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ActivityAdapter.MainHolder(binding)
    }

    override fun onBindViewHolder(holder: ActivityAdapter.MainHolder, position: Int) {
        val activity= activities[holder.adapterPosition]
        holder.bind(activity, listener)
    }

    override fun getItemCount(): Int = activities.size

    class MainHolder(private val binding: CardActivityBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(activity: Activity, listener: ActivityListener) {
            binding.activityTitle.text = activity.type.typeName
            binding.activityDescription.text = activity.type.description
            binding.startDate.text = activity.start?.toString()
            binding.stopDate.text = activity.stop?.toString()
            binding.root.setOnClickListener { listener.onActivityClick(activity) }
        }
    }
}