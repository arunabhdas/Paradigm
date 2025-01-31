package app.paradigmatic.paradigmaticapp.data.routeplannertool

import app.paradigmatic.paradigmaticapp.domain.model.taskstop.TaskRoute


fun TaskRouteEntity.toTaskRoute(): TaskRoute {
    return TaskRoute(
        title = title,
        notes = notes,
        stops = stops,
        id = id
    )
}

fun TaskRoute.toTaskRouteEntity(): TaskRouteEntity {
    return TaskRouteEntity(
        title = title,
        notes = notes,
        stops = stops,
        id = id
    )
}