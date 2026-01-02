package com.example.caller_dial.domain.model

data class CallContact(
    val id: Long = 0,
    val listId: Long,
    val phoneNumber: String,
    val status: CallStatus = CallStatus.PENDING,
    val attempts: Int = 0,
    val note: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)
