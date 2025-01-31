package app.paradigmatic.paradigmaticapp.data.routeplannertool

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [TaskStopEntity::class, TaskRouteEntity::class],
    version = 5
)

@TypeConverters(TaskStopListConverter::class)
abstract class RoutePlannerToolDatabase: RoomDatabase() {
    abstract val taskStopDao: TaskStopDao
    abstract val taskRouteDao: TaskRouteDao
}