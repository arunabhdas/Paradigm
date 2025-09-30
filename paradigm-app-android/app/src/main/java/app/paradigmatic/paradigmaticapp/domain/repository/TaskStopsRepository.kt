package app.paradigmatic.paradigmaticapp.domain.repository

import app.paradigmatic.paradigmaticapp.domain.model.taskstop.TaskStop
import kotlinx.coroutines.flow.Flow

interface TaskStopsRepository {

    suspend fun insertTaskStop(taskStop: TaskStop)

    suspend fun deleteTaskStop(taskStop: TaskStop)

    fun getTaskStops(): Flow<List<TaskStop>>
}