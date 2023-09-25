package com.example.iteneraryapplication.dashboard.shared.data

import android.net.Uri
import android.util.Log
import com.example.iteneraryapplication.app.shared.state.AppUiState
import com.example.iteneraryapplication.app.shared.state.AppUiStateModel
import com.example.iteneraryapplication.app.shared.state.ShowAppUiLoading
import com.example.iteneraryapplication.dashboard.shared.domain.data.Notes
import com.example.iteneraryapplication.dashboard.shared.presentation.DashboardState
import com.example.iteneraryapplication.dashboard.shared.presentation.GetNotesTypeData
import com.example.iteneraryapplication.dashboard.shared.presentation.ShowDashboardDismissLoading
import com.example.iteneraryapplication.dashboard.shared.presentation.ShowDashboardError
import com.example.iteneraryapplication.dashboard.shared.presentation.ShowDashboardLoading
import com.example.iteneraryapplication.dashboard.shared.presentation.ShowDashboardNoData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import java.lang.Thread.State
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DashboardRepository @Inject constructor(
    private val fireStore: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage,
    firebaseUser: FirebaseUser?
) {

    private val userId = firebaseUser?.uid ?: error("failed to fetch userId")

    suspend fun saveNotes(notesType: String, notes: Notes) : Boolean {
        val saveDetailsStatus = fireStore.collection("notes")
            .document(userId)
            .collection(notesType)
            .add(notes)
        saveDetailsStatus.await()
        return saveDetailsStatus.isSuccessful
    }

    fun getNotes(notesType: String, getNotesItem: (appUiState: DashboardState) -> Unit) {
        val fireStorePath = fireStore.collection("notes")
            .document(userId)
            .collection(notesType)
            .orderBy("notesDateSaved", Query.Direction.DESCENDING)

        fireStorePath.addSnapshotListener { result, error ->
            val resultNotes = mutableListOf<Notes>()
            if (error == null) result?.forEach { resultNotes.add(it.toObject(Notes::class.java)) }
            getNotesItem.invoke(GetNotesTypeData(listNotes = resultNotes))
        }
    }

    suspend fun saveNoteImage(notesType: String, imageUri: Uri) : String {
        val ref: StorageReference = firebaseStorage.reference
            .child("noteImages")
            .child(userId)
            .child(notesType)
            .child(UUID.randomUUID().toString())

        ref.putFile(imageUri).await().also {
            return if (it.error == null) ref.downloadUrl.await().toString()
            else error("failed to upload image")
        }
    }
}