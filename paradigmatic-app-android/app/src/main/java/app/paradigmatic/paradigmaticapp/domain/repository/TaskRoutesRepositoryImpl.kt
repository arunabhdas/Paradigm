package app.paradigmatic.paradigmaticapp.domain.repository

import app.paradigmatic.paradigmaticapp.data.routeplannertool.TaskRouteDao
import app.paradigmatic.paradigmaticapp.data.routeplannertool.toTaskRoute
import app.paradigmatic.paradigmaticapp.data.routeplannertool.toTaskRouteEntity
import app.paradigmatic.paradigmaticapp.domain.model.taskstop.TaskRoute
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TaskRoutesRepositoryImpl(
    private val taskRouteDao: TaskRouteDao
): TaskRoutesRepository {
    override suspend fun insertTaskRoute(taskRoute: TaskRoute) {
        taskRouteDao.insertTaskRoute(taskRoute.toTaskRouteEntity())
    }

    override suspend fun deleteTaskRoute(taskRoute: TaskRoute) {
        taskRouteDao.deleteTaskRoute(taskRoute.toTaskRouteEntity())
    }

    override fun getTaskRoutes(): Flow<List<TaskRoute>> {
        return taskRouteDao.getTaskRoutes().map { taskRoutes ->
            taskRoutes.map {it.toTaskRoute()}
        }
    }
}