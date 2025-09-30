package app.paradigmatic.paradigmaticapp.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WorkOrder(
    @SerialName("id")
    val id: String,

    @SerialName("user_id")
    val user_id: String,

    @SerialName("title")
    val title: String,
    
    @SerialName("description")
    val description: String,
    
    @SerialName("status")
    val status: String,

    @SerialName("created_at")
    val createdAt: String
    
)