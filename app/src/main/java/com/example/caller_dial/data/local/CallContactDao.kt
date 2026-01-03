package com.example.caller_dial.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CallContactDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(contacts: List<CallContactEntity>)

    @Query("SELECT * FROM call_contacts WHERE listId = :listId")
    fun getContactsForList(listId: Long): Flow<List<CallContactEntity>>

    @Query("""UPDATE call_contacts SET status = :status, note = :note, attempts = :attempts WHERE id = :contactId""")
    suspend fun updateCallStatus(
        contactId: Long,
        status: String,
        note: String?,
        attempts: Int
    )

    @Query("SELECT COUNT(*) FROM call_contacts WHERE listId = :listId")
    suspend fun countTotal(listId: Long): Int

    @Query("""SELECT COUNT(*) FROM call_contacts WHERE listId = :listId AND status = :status""")
    suspend fun countByStatus(listId: Long, status: String): Int

    @Query("DELETE FROM call_contacts WHERE listId = :listId")
    suspend fun deleteContactsForList(listId: Long)
}
