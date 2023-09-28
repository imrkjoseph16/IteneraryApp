package com.example.iteneraryapplication.app.widget

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.iteneraryapplication.R
import com.example.iteneraryapplication.R.color.ColorBlueNote
import com.example.iteneraryapplication.R.color.ColorYellowNote
import com.example.iteneraryapplication.R.color.ColorRedNote
import com.example.iteneraryapplication.R.color.ColorOrangeNote
import com.example.iteneraryapplication.R.color.ColorBlackNote
import com.example.iteneraryapplication.R.color.ColorGreenNote
import com.example.iteneraryapplication.app.extension.setVisible
import com.example.iteneraryapplication.app.foundation.BaseBottomSheetFragment
import com.example.iteneraryapplication.app.util.Default.Companion.ACTION
import com.example.iteneraryapplication.app.util.Default.Companion.ACTION_DELETE
import com.example.iteneraryapplication.app.util.Default.Companion.ACTION_HAND_WRITING
import com.example.iteneraryapplication.app.util.Default.Companion.ACTION_IMAGE
import com.example.iteneraryapplication.app.util.Default.Companion.ACTION_SELECTED_COLOR
import com.example.iteneraryapplication.app.util.Default.Companion.ACTION_WEB_URL
import com.example.iteneraryapplication.app.util.Default.Companion.BOTTOM_SHEET_ACTION
import com.example.iteneraryapplication.app.util.Default.Companion.SELECTED_COLOR
import com.example.iteneraryapplication.databinding.BottomSheetNotesOptionsBinding

class NoteBottomSheet : BaseBottomSheetFragment<BottomSheetNotesOptionsBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> BottomSheetNotesOptionsBinding
        get() = BottomSheetNotesOptionsBinding::inflate

    override val inflater: (LayoutInflater) -> BottomSheetNotesOptionsBinding
        get() = BottomSheetNotesOptionsBinding::inflate

    override fun onBottomSheetCreated() {
        super.onBottomSheetCreated()
        binding.apply { 
            configureViews()
        }
    }
    
    private fun BottomSheetNotesOptionsBinding.configureViews() {
        layoutDeleteNote.setVisible(existingNotes)

        fNote1.setOnClickListener {
            configureNotes(selectedImage = 0)
        }

        fNote2.setOnClickListener {
            configureNotes(selectedImage = 1)
        }

        fNote4.setOnClickListener {
            configureNotes(selectedImage = 2)
        }

        fNote5.setOnClickListener {
            configureNotes(selectedImage = 3)
        }

        fNote6.setOnClickListener {
            configureNotes(selectedImage = 4)
        }

        fNote7.setOnClickListener {
            configureNotes(selectedImage = 5)
        }

        // Bottom Option Listener
        layoutImage.setOnClickListener {
            sendBroadCastIntent(actionValue = ACTION_IMAGE, canDismiss = true)
        }

        layoutWebUrl.setOnClickListener {
            sendBroadCastIntent(actionValue = ACTION_WEB_URL, canDismiss = true)
        }

        layoutHandWriting.setOnClickListener {
            sendBroadCastIntent(actionValue = ACTION_HAND_WRITING, canDismiss = true)
        }

        layoutDeleteNote.setOnClickListener {
            sendBroadCastIntent(actionValue = ACTION_DELETE, canDismiss = true)
        }
    }

    @SuppressLint("ResourceType")
    private fun BottomSheetNotesOptionsBinding.configureNotes(selectedImage: Int) {
        
        val imageNoteList = buildList {
            add(ImageNoteSetup(imgNote1, resources.getString(ColorBlueNote)))
            add(ImageNoteSetup(imgNote2, resources.getString(ColorYellowNote)))
            add(ImageNoteSetup(imgNote3, resources.getString(ColorRedNote)))
            add(ImageNoteSetup(imgNote4, resources.getString(ColorGreenNote)))
            add(ImageNoteSetup(imgNote5, resources.getString(ColorOrangeNote)))
            add(ImageNoteSetup(imgNote6, resources.getString(ColorBlackNote)))
        }

        imageNoteList.onEachIndexed { currentImage, imageNoteSetup ->
            imageNoteSetup.imageView.setImageResource(
                if (currentImage == selectedImage) R.drawable.icon_tick_24px
                else 0
            )
        }

        sendBroadCastIntent(
            actionValue = ACTION_SELECTED_COLOR,
            extraSelectedColor = imageNoteList[selectedImage].color
        )
    }

    private fun sendBroadCastIntent(
        actionValue: String,
        extraSelectedColor: String? = null,
        canDismiss: Boolean = false
    ) {
        val intent = Intent(BOTTOM_SHEET_ACTION)
        intent.putExtra(ACTION, actionValue)
        intent.putExtra(SELECTED_COLOR, extraSelectedColor)
        LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
        if (canDismiss) dismiss()
    }

    data class ImageNoteSetup(
        val imageView: ImageView,
        val color: String
    )

    companion object {
        var existingNotes = false

        fun createInstance(isExistingNotes: Boolean) = NoteBottomSheet().apply {
            existingNotes = isExistingNotes
        }
    }
}