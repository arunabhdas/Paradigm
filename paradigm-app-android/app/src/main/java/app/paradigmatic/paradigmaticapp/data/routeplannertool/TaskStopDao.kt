package app.paradigmatic.paradigmaticapp.data.routeplannertool

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskStopDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTaskStop(taskStop: TaskStopEntity)

    @Delete
    suspend fun deleteTaskStop(taskStop: TaskStopEntity)

    @Query("SELECT * FROM taskstopentity")
    fun getTaskStops(): Flow<List<TaskStopEntity>>

}