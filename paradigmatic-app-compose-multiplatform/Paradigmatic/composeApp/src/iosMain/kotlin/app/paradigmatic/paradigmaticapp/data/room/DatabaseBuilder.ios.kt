package app.paradigmatic.paradigmaticapp.data.room

import androidx.room.RoomDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask
import androidx.room.Room

fun getDatabaseBuilder(): RoomDatabase.Builder<MemeDatabase> {
    val dbFile = documentDirectory() + "/meme.db"
    return Room.databaseBuilder(
        name = dbFile
    )
}

@OptIn(ExperimentalForeignApi::class)
private fun documentDirectory(): String {
    val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null
    )
    return requireNotNull(documentDirectory?.path)
}