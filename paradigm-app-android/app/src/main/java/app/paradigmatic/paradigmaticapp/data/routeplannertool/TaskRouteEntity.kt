package app.paradigmatic.paradigmaticapp.data.routeplannertool

import app.paradigmatic.paradigmaticapp.domain.model.taskstop.TaskStop
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TaskRouteEntity(
    val title: String,
    val notes: String,
    val stops: List<TaskStop>,
    @PrimaryKey val id: Int? = null
)
