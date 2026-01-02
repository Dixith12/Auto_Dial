package com.example.caller_dial.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "call_contacts")
data class CallContactEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val listId: Long,
    val phoneNumber: String,
    val status: String,
    val attempts: Int,
    val note: String?,
    val timestamp: Long
)
