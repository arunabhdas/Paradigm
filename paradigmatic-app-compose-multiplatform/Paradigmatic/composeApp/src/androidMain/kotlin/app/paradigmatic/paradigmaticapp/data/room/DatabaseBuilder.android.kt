package app.paradigmatic.paradigmaticapp.data.room

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

fun getDatabaseBuilder(context: Context): RoomDatabase.Builder<MemeDatabase> {
    val dbFile = context.getDatabasePath("meme.db")
    return Room.databaseBuilder(
        context = context,
        name = dbFile.absolutePath
    )
}