package com.example.iteneraryapplication.dashboard.shared.data

import android.net.Uri
import com.example.iteneraryapplication.dashboard.shared.domain.data.Notes
import com.example.iteneraryapplication.dashboard.shared.presentation.DashboardState
import com.example.iteneraryapplication.dashboard.shared.presentation.ShowDashboardDismissLoading
import com.example.iteneraryapplication.dashboard.shared.presentation.ShowDashboardError
import com.example.iteneraryapplication.dashboard.shared.presentation.ShowDashboardImageError
import com.example.iteneraryapplication.dashboard.shared.presentation.ShowDashboardLoading
import com.example.iteneraryapplication.dashboard.shared.presentation.ShowDashboardNoData
import com.example.iteneraryapplication.dashboard.shared.presentation.ShowGetNoteSuccess
import com.example.iteneraryapplication.dashboard.shared.presentation.ShowSaveImageSuccess
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await
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

    // These data streams state flow,
    // will collect all the current state and return it on the view.
    private val _dataStream = MutableStateFlow<DashboardState>(ShowDashboardNoData)
    fun getDashboardStream(): StateFlow<DashboardState> = _dataStream

    suspend fun saveNotes(notesType: String, notes: Notes) : Boolean {
        val saveDetailsStatus = fireStore.collection("notes")
            .document(userId)
            .collection(notesType)
            .add(notes)
        saveDetailsStatus.await()
        return saveDetailsStatus.isSuccessful
    }

    fun getNotes(notesType: String) {
        _dataStream.apply {
            value = ShowDashboardLoading

            fireStore.collection("notes")
            .document(userId)
            .collection(notesType).addSnapshotListener { result, error ->
                val listNotes = mutableListOf<Notes>()

                if (error != null) value = ShowDashboardError(error)
                else result?.forEach { listNotes.add(it.toObject(Notes::class.java)) }

                value = ShowGetNoteSuccess(notes = listNotes)
            }

            value = ShowDashboardDismissLoading
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