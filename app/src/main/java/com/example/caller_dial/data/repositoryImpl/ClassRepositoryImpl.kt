package com.example.caller_dial.data.repository

import com.example.caller_dial.data.local.*
import com.example.caller_dial.domain.model.*
import com.example.caller_dial.domain.repository.CallRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CallRepositoryImpl @Inject constructor(
    private val listDao: CallListDao,
    private val contactDao: CallContactDao
) : CallRepository {

    override suspend fun createList(name: String) {
        listDao.insert(
            CallListEntity(
                name = name,
                createdAt = System.currentTimeMillis()
            )
        )
    }

    override fun getAllLists(): Flow<List<CallList>> =
        listDao.getAllLists().map { entities ->
            entities.map {
                CallList(
                    id = it.id,
                    name = it.name,
                    createdAt = it.createdAt
                )
            }
        }

    override fun getContacts(listId: Long): Flow<List<CallContact>> =
        contactDao.getContactsForList(listId).map { entities ->
            entities.map {
                CallContact(
                    id = it.id,
                    listId = it.listId,
                    phoneNumber = it.phoneNumber,
                    status = CallStatus.valueOf(it.status),
                    attempts = it.attempts,
                    note = it.note,
                    timestamp = it.timestamp
                )
            }
        }

    override suspend fun insertContacts(
        listId: Long,
        contacts: List<CallContact>
    ) {
        contactDao.insertAll(
            contacts.map {
                CallContactEntity(
                    listId = listId,
                    phoneNumber = it.phoneNumber,
                    status = it.status.name,
                    attempts = it.attempts,
                    note = it.note,
                    timestamp = it.timestamp
                )
            }
        )
    }

    override suspend fun updateContact(
        contactId: Long,
        status: CallStatus,
        note: String?,
        attempts: Int
    ) {
        contactDao.updateCallStatus(
            contactId,
            status.name,
            note,
            attempts
        )
    }

    override suspend fun getSummary(listId: Long): CallSummary =
        CallSummary(
            total = contactDao.countTotal(listId),
            answered = contactDao.countByStatus(listId, CallStatus.ANSWERED.name),
            unanswered = contactDao.countByStatus(listId, CallStatus.UNANSWERED.name)
        )
}
