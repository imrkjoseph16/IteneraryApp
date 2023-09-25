package com.example.iteneraryapplication.dashboard.shared.data

import com.example.iteneraryapplication.dashboard.shared.domain.data.Notes
import com.example.iteneraryapplication.dashboard.shared.presentation.DashboardState
import com.example.iteneraryapplication.dashboard.shared.presentation.ShowDashboardDismissLoading
import com.example.iteneraryapplication.dashboard.shared.presentation.ShowDashboardError
import com.example.iteneraryapplication.dashboard.shared.presentation.ShowDashboardLoading
import com.example.iteneraryapplication.dashboard.shared.presentation.ShowDashboardNoData
import com.example.iteneraryapplication.dashboard.shared.presentation.ShowGetNoteSuccess
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DashboardRepository @Inject constructor(
    private val fireStore: FirebaseFirestore,
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

                value = ShowGetNoteSuccess(listNotes)
            }

            value = ShowDashboardDismissLoading
        }
    }
}