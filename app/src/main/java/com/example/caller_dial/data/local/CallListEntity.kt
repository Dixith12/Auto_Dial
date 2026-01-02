package com.example.caller_dial.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "call_lists")
data class CallListEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val createdAt: Long
)
