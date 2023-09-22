package com.example.iteneraryapplication.app.local.domain

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.iteneraryapplication.app.util.Default.Companion.DB_TRAVEL_ITINERARY_PATH
import com.example.iteneraryapplication.app.local.data.form.NoteForm
import com.example.iteneraryapplication.app.local.data.gateway.dao.NoteDao

@Database(entities = [NoteForm::class], version = 1)
abstract class RoomDatabaseService : RoomDatabase() {

    companion object {

        @Volatile
        private var DB_INSTANCE: RoomDatabaseService? = null

        fun getInstance(context: Context) : RoomDatabaseService {
            return DB_INSTANCE ?: synchronized(this) {
                DB_INSTANCE ?: buildDatabase(context).also { DB_INSTANCE = it }
            }
        }

        private fun buildDatabase(context: Context) : RoomDatabaseService {
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RoomDatabaseService::class.java,
                    DB_TRAVEL_ITINERARY_PATH)
                    .allowMainThreadQueries()
                    .build()

                DB_INSTANCE = instance
                return instance
            }
        }
    }

    abstract fun commandDao() : NoteDao
}