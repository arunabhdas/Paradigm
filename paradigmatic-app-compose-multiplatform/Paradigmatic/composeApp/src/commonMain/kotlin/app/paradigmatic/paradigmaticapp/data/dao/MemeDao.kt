package app.paradigmatic.paradigmaticapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import app.paradigmatic.paradigmaticapp.domain.model.Meme
import kotlinx.coroutines.flow.Flow

@Dao
interface MemeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeme(meme: Meme)

    @Update
    suspend fun updateMeme(meme: Meme)

    @Transaction
    @Query("SELECT * FROM meme WHERE _id = :memeId")
    suspend fun getMemeById(memeId: Int): Meme

    @Transaction
    @Query("SELECT * FROM meme WHERE _id = :memeId")
    fun getMemeByIdFlow(memeId: Int): Flow<Meme?>

    @Transaction
    @Query("SELECT * FROM meme ORDER BY isFavorite DESC")
    fun readAllMemesSortByFavorite(): Flow<List<Meme>>

}