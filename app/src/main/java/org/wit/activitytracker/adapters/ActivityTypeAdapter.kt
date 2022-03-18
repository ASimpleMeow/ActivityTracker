package org.wit.activitytracker.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.wit.activitytracker.databinding.CardActivityTypeBinding
import org.wit.activitytracker.models.ActivityType

interface ActivityTypeListener {
    fun onActivityTypeClick(activityType: ActivityType)
}

class ActivityTypeAdapter constructor(private val activityTypes: List<ActivityType>,
                                  private val listener: ActivityTypeListener)
    : RecyclerView.Adapter<ActivityTypeAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardActivityTypeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val activityType = activityTypes[holder.adapterPosition]
        holder.bind(activityType, listener)
    }

    override fun getItemCount(): Int = activityTypes.size

    class MainHolder(private val binding: CardActivityTypeBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(activityType: ActivityType, listener: ActivityTypeListener) {
            binding.activityTypeTitle.text = activityType.typeName
            binding.description.text = activityType.description
            binding.root.setOnClickListener { listener.onActivityTypeClick(activityType) }
        }
    }
}