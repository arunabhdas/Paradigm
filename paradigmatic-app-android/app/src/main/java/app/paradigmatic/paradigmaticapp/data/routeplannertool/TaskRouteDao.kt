package app.paradigmatic.paradigmaticapp.data.routeplannertool

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskRouteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTaskRoute(taskRoute: TaskRouteEntity)

    @Delete
    suspend fun deleteTaskRoute(taskRoute: TaskRouteEntity)

    @Query("SELECT * FROM taskrouteentity")
    fun getTaskRoutes(): Flow<List<TaskRouteEntity>>
}