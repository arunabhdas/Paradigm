package app.paradigmatic.paradigmaticapp.data.routeplannertool

import app.paradigmatic.paradigmaticapp.domain.model.taskstop.TaskStop
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class TaskStopListConverter {
    @TypeConverter
    fun fromTaskStopList(value: List<TaskStop>?): String {
        val gson = Gson()
        val type: Type = object : TypeToken<List<TaskStop>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toTaskStopList(value: String?): List<TaskStop>? {
        val gson = Gson()
        val type: Type = object : TypeToken<List<TaskStop>>() {}.type
        return gson.fromJson(value, type)
    }
}