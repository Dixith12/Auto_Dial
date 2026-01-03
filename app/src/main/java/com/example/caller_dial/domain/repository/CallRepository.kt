package com.example.caller_dial.domain.repository

import com.example.caller_dial.domain.model.*
import kotlinx.coroutines.flow.Flow

interface CallRepository {

    suspend fun createList(name: String):Long
    fun getAllLists(): Flow<List<CallList>>
    fun getContacts(listId: Long): Flow<List<CallContact>>
    suspend fun insertContacts(listId: Long, contacts: List<CallContact>)
    suspend fun updateContact(
        contactId: Long,
        status: CallStatus,
        note: String?,
        attempts: Int
    )
    suspend fun getSummary(listId: Long): CallSummary
    suspend fun deleteList(listId: Long)

}
