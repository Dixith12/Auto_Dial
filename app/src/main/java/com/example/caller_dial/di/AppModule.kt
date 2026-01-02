package com.example.caller_dial.di

import android.content.Context
import androidx.room.Room
import com.example.caller_dial.data.local.*
import com.example.caller_dial.data.repository.CallRepositoryImpl
import com.example.caller_dial.domain.repository.CallRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "caller_dial_db"
        ).build()

    @Provides
    fun provideCallListDao(db: AppDatabase): CallListDao =
        db.callListDao()

    @Provides
    fun provideCallContactDao(db: AppDatabase): CallContactDao =
        db.callContactDao()

    @Provides
    @Singleton
    fun provideRepository(
        callListDao: CallListDao,
        callContactDao: CallContactDao
    ): CallRepository =
        CallRepositoryImpl(callListDao, callContactDao)
}
