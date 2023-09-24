package com.example.iteneraryapplication.dashboard.shared.data

import android.util.Log
import com.example.iteneraryapplication.dashboard.shared.domain.data.Notes
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.gson.Gson
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class DashboardRepository @Inject constructor(
    private val fireStore: FirebaseFirestore,
    private val firebaseUser: FirebaseUser?
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

    suspend fun getNotes(notesType: String) : List<Notes?> {
        val getNotes = fireStore.collection("notes")
            .document(userId)
            .collection(notesType).get()
        getNotes.await()

        val mappedList = getNotes.result.documents.mapIndexed { _, documentSnapshot ->
            return@mapIndexed documentSnapshot.toObject(Notes::class.java)
        }

        return mappedList
    }
}