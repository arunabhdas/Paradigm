package app.paradigmatic.paradigmaticapp.domain.model.taskstop

data class TaskRoute (
    val title: String,
    val notes: String,
    val stops: List<TaskStop>,
    val id: Int? = null

)