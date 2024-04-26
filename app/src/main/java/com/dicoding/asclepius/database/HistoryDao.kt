package com.dicoding.asclepius.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(history: History)

    @Query("SELECT * FROM history ORDER BY imageUri ASC")
    fun getAll(): LiveData<List<History>>

    @Query("DELETE FROM history")
    fun deleteAll()
}