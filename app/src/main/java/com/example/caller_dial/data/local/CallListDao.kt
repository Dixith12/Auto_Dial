package com.example.caller_dial.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CallListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(list: CallListEntity): Long

    @Query("SELECT * FROM call_lists ORDER BY createdAt DESC")
    fun getAllLists(): Flow<List<CallListEntity>>

    @Query("DELETE FROM call_lists WHERE id = :listId")
    suspend fun deleteList(listId: Long)

}
