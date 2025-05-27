package com.felipemz.inventaryapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.felipemz.inventaryapp.data.local.entity.MovementItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovementDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movement: MovementItemEntity): Long

    @Query("SELECT * FROM movements ORDER BY date DESC, time DESC")
    fun getAll(): Flow<List<MovementItemEntity>>
}