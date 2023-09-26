package com.example.iteneraryapplication.preview

import android.content.Intent
import android.view.LayoutInflater
import androidx.core.os.bundleOf
import com.example.iteneraryapplication.app.foundation.BaseActivity
import com.example.iteneraryapplication.app.shared.dto.layout.NoteItemViewDto
import com.example.iteneraryapplication.dashboard.shared.presentation.CreateTravelNote
import com.example.iteneraryapplication.dashboard.shared.presentation.CreateTravelNote.Companion.TRAVEL_NOTES_TYPE_SELECTED
import com.example.iteneraryapplication.databinding.ActivityPreviewImageBinding
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PreviewNotesDetails : BaseActivity<ActivityPreviewImageBinding>() {

    override val inflater: (LayoutInflater) -> ActivityPreviewImageBinding
        get() = ActivityPreviewImageBinding::inflate

    private val notesTypeSelected by lazy {
        intent.getStringExtra(TRAVEL_NOTES_TYPE_SELECTED)
    }

    private val notesData by lazy {
        Gson().fromJson(intent.getStringExtra(EXTRA_DATA_NOTES), NoteItemViewDto::class.java)
    }

    override fun onActivityCreated() {
        super.onActivityCreated()
        binding.apply { configureViews() }
    }

    private fun ActivityPreviewImageBinding.configureViews() {
        data = notesData

        imageClose.setOnClickListener { onBackPressed() }
        editDetails.setOnClickListener {
            openTravelNoteScreen().also {  }
        }
        executePendingBindings()
    }

    private fun openTravelNoteScreen() = navigationUtil.navigateActivity(
        context = this,
        noHistory = true,
        className = CreateTravelNote::class.java,
        bundle = bundleOf(
            EXTRA_DATA_NOTES to Gson().toJson(notesData),
            TRAVEL_NOTES_TYPE_SELECTED to notesTypeSelected
        ),
    )

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CLEAR_HISTORY) finish()
    }

    companion object {
        const val EXTRA_DATA_NOTES = "extra_data_notes"
        const val REQUEST_CODE_CLEAR_HISTORY = 0
    }
}