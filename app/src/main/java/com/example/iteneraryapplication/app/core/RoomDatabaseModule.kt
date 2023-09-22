package com.example.iteneraryapplication.app.core

import android.content.Context
import com.example.iteneraryapplication.app.local.domain.RoomDatabaseService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RoomDatabaseModule {

    @Singleton
    @Provides
    fun executeRoomInstance(
        @ApplicationContext
        context: Context,
    ) = RoomDatabaseService.getInstance(context)

    @Singleton
    @Provides
    fun executeCommandDao(
        commandService: RoomDatabaseService
    ) = commandService.commandDao()
}