package app.paradigmatic.paradigmaticapp.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meme")
data class Meme (
    @PrimaryKey(autoGenerate = true)
    val _id: Int = 0,
    val image: String,
    val title: String,
    val description: String,
    val isFavorite: Boolean,
    val category: String,
    val tags: String,
    val creator: String
)