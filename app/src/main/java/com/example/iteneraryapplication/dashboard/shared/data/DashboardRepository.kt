package com.example.iteneraryapplication.dashboard.shared.data

import android.net.Uri
import android.widget.Toast
import com.example.iteneraryapplication.app.util.ViewUtil.Companion.generateRandomCharacters
import com.example.iteneraryapplication.dashboard.shared.domain.data.Notes
import com.example.iteneraryapplication.dashboard.shared.presentation.DashboardState
import com.example.iteneraryapplication.dashboard.shared.presentation.GetNotesTypeData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
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

    suspend fun saveNotes(notesType: String, notes: Notes) : Boolean {
        val saveDetailsStatus = fireStore.collection("notes")
            .document(userId)
            .collection(notesType)
            .document(notes.itemId ?: generateRandomCharacters())
            .set(notes)
        saveDetailsStatus.await()
        return saveDetailsStatus.isSuccessful
    }

    suspend fun deleteNotes(notesType: String? = null, notes: Notes) : Boolean {
        val deleteDetailsStatus = fireStore.collection("notes")
            .document(userId)
            .collection(notesType ?: error("notesType is required"))
            .document(notes.itemId ?: error("itemId not found"))
            .delete()
        deleteDetailsStatus.await()
        return deleteDetailsStatus.isSuccessful
    }

    fun getNotes(notesType: String, getNotesItem: (appUiState: DashboardState) -> Unit) {
        val fireStorePath = fireStore.collection("notes")
            .document(userId)
            .collection(notesType)
            .orderBy("notesTimeStampSaved", Query.Direction.DESCENDING)

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

    suspend fun deleteNoteImage(imageUrl: String?) : Boolean {
        val imageRef = firebaseStorage.getReferenceFromUrl(imageUrl.orEmpty()).delete()
        imageRef.await()

        return imageRef.isSuccessful
    }
}