package org.wit.activitytracker.models

interface ActivityStore {
    fun create(activity: Activity)
    fun update(activity: Activity)
    fun delete(id: Long)
    fun find(id: Long): Activity?
    fun findAll(): List<Activity>
}