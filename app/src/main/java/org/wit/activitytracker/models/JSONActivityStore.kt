package org.wit.activitytracker.models

import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import org.wit.activitytracker.utils.exists
import org.wit.activitytracker.utils.read
import org.wit.activitytracker.utils.write
import java.lang.reflect.Type
import java.util.*

const val JSON_FILE = "activities.json"
val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, UriParser())
    .create()

val listType: Type = object : TypeToken<ArrayList<Activity>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class JSONActivityStore(private val context: Context): ActivityStore {

    var activities = mutableListOf<Activity>()

    init {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(activities, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        activities = gsonBuilder.fromJson(jsonString, listType)
    }

    override fun create(activity: Activity) {
        activity.id = generateRandomId()
        activities.add(activity)
        serialize()
    }

    override fun update(activity: Activity) {
        val existing = find(activity.id)
        if (existing != null) {
            activities.removeAt(activities.indexOfFirst { it.id == activity.id })
            activities.add(activity)
        } else {
            create(activity)
        }
        serialize()
    }

    override fun delete(id: Long) {
        val activity = find(id)
        if (activity != null) {
            activities.remove(activity)
            serialize()
        }
    }

    override fun find(id: Long): Activity? {
        return activities.find { it.id == id }
    }

    override fun findAll(): List<Activity> {
        return activities;
    }
}

class UriParser : JsonDeserializer<Uri>, JsonSerializer<Uri> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Uri {
        return Uri.parse(json?.asString)
    }

    override fun serialize(
        src: Uri?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src.toString())
    }
}