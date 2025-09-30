package app.paradigmatic.paradigmaticapp.domain.repository

import app.paradigmatic.paradigmaticapp.domain.model.taskstop.TaskRoute
import kotlinx.coroutines.flow.Flow

interface TaskRoutesRepository {
    suspend fun insertTaskRoute(taskStop: TaskRoute)

    suspend fun deleteTaskRoute(taskStop: TaskRoute)

    fun getTaskRoutes(): Flow<List<TaskRoute>>
}