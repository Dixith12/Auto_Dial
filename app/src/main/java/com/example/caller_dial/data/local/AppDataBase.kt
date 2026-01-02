package com.example.caller_dial.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        CallListEntity::class,
        CallContactEntity::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun callListDao(): CallListDao
    abstract fun callContactDao(): CallContactDao
}
