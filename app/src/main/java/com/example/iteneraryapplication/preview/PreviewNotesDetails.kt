package com.example.iteneraryapplication.preview

import android.view.LayoutInflater
import com.example.iteneraryapplication.app.foundation.BaseActivity
import com.example.iteneraryapplication.app.shared.dto.layout.NoteItemViewDto
import com.example.iteneraryapplication.databinding.ActivityPreviewImageBinding
import com.google.gson.Gson

class PreviewNotesDetails : BaseActivity<ActivityPreviewImageBinding>() {

    override val inflater: (LayoutInflater) -> ActivityPreviewImageBinding
        get() = ActivityPreviewImageBinding::inflate

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
        executePendingBindings()
    }

    companion object {
        const val EXTRA_DATA_NOTES = "extra_data_notes"
    }
}