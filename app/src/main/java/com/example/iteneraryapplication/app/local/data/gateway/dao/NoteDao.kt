package com.example.iteneraryapplication.app.local.data.gateway.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.iteneraryapplication.app.local.data.form.NoteForm
import com.example.iteneraryapplication.app.util.Default.Companion.DB_TRAVEL_PLANNING_TABLE

@Dao
interface NoteDao {

    @Query("Select * from $DB_TRAVEL_PLANNING_TABLE")
    fun getNoteList() : MutableList<NoteForm>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveTravelNotes(noteForm: NoteForm)
}