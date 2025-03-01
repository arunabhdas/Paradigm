package app.paradigmatic.paradigmaticapp.data.room

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.InvalidationTracker
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverter
import app.paradigmatic.paradigmaticapp.domain.model.Meme
import app.paradigmatic.paradigmaticapp.data.dao.MemeDao
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json

@Database(
    entities = [Meme::class],
    version = 1,
    exportSchema = true
)

@ConstructedBy(MemeDatabaseConstructor::class)
abstract class MemeDatabase: RoomDatabase() {
    abstract fun memeDao(): MemeDao
}

@Suppress(
    "NO_ACTUAL_FOR_EXPECT"
)
expect object MemeDatabaseConstructor: RoomDatabaseConstructor<MemeDatabase> {
    override fun initialize(): MemeDatabase
}

class MemeTypeConverter {
    @TypeConverter
    fun fromString(value: String): List<String> {
        return Json.decodeFromString(
            ListSerializer(String.serializer()),
            value
        )
    }

    @TypeConverter
    fun fromList(list: List<String>): String {
        return Json.encodeToString(
            ListSerializer(String.serializer()),
            list
        )
    }

}