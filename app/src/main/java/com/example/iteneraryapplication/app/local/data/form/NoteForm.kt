package com.example.iteneraryapplication.app.local.data.form

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.iteneraryapplication.app.util.Default.Companion.DB_TRAVEL_PLANNING_DESC
import com.example.iteneraryapplication.app.util.Default.Companion.DB_TRAVEL_PLANNING_TABLE
import com.example.iteneraryapplication.app.util.Default.Companion.DB_TRAVEL_PLANNING_TITLE

@Entity(tableName = DB_TRAVEL_PLANNING_TABLE)
data class NoteForm(

    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,

    @ColumnInfo(name = DB_TRAVEL_PLANNING_TITLE)
    var title: String? = null,

    @ColumnInfo(name = DB_TRAVEL_PLANNING_DESC)
    var description: String? = null
)