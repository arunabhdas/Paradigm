package app.paradigmatic.paradigmaticapp.data.routeplannertool

import app.paradigmatic.paradigmaticapp.domain.model.taskstop.TaskStop

fun TaskStopEntity.toTaskStop(): TaskStop {
    return TaskStop(
        title = title,
        description = description,
        formattedAddressOrigin = formattedAddressOrigin,
        latOrigin = latOrigin,
        lngOrigin = lngOrigin,
        formattedAddressDestination = formattedAddressDestination,
        latDestination = latDestination,
        lngDestination = lngDestination,
        id = id
    )
}

fun TaskStop.toTaskStopEntity(): TaskStopEntity {
    return TaskStopEntity(
        title = title,
        description = description,
        formattedAddressOrigin = formattedAddressOrigin,
        latOrigin = latOrigin,
        lngOrigin = lngOrigin,
        formattedAddressDestination = formattedAddressDestination,
        latDestination = latDestination,
        lngDestination = lngDestination,
        id = id
    )
}