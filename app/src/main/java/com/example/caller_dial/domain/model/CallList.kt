package com.example.caller_dial.domain.model

data class CallList(
    val id: Long = 0,
    val name: String,
    val createdAt: Long = System.currentTimeMillis()
)
