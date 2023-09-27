package com.example.iteneraryapplication.app.shared.component

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
import com.example.iteneraryapplication.R.color.ColorWhiteNote
import com.example.iteneraryapplication.R.color.ColorOrangeNote
import com.example.iteneraryapplication.R.color.ColorBlackNote
import com.example.iteneraryapplication.app.extension.setVisible
import com.example.iteneraryapplication.app.foundation.BaseBottomSheetFragment
import com.example.iteneraryapplication.app.util.Default.Companion.ACTION_DELETE
import com.example.iteneraryapplication.app.util.Default.Companion.ACTION_HAND_WRITING
import com.example.iteneraryapplication.app.util.Default.Companion.ACTION_IMAGE
import com.example.iteneraryapplication.app.util.Default.Companion.ACTION_WEB_URL
import com.example.iteneraryapplication.databinding.BottomSheetNotesOptionsBinding

class NoteBottomSheet : BaseBottomSheetFragment<BottomSheetNotesOptionsBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> BottomSheetNotesOptionsBinding
        get() = BottomSheetNotesOptionsBinding::inflate

    override val inflater: (LayoutInflater) -> BottomSheetNotesOptionsBinding
        get() = BottomSheetNotesOptionsBinding::inflate

    private var selectedColor = "#333333"

    override fun onBottomSheetCreated() {
        super.onBottomSheetCreated()
        binding.apply { 
            configureViews()
        }
    }
    
    private fun BottomSheetNotesOptionsBinding.configureViews() {
        layoutDeleteNote.setVisible(existingNotes)

        fNote1.setOnClickListener {
            configureNotes(selectedImage = 0).also {
                sendBroadCastIntent(
                    extraValue = "Blue",
                    secondExtraValue = selectedColor
                )
            }
        }

        fNote2.setOnClickListener {
            configureNotes(selectedImage = 1).also {
                sendBroadCastIntent(
                    extraValue = "Yellow",
                    secondExtraValue = selectedColor
                )
            }
        }

        fNote4.setOnClickListener {
            configureNotes(selectedImage = 2).also {
                sendBroadCastIntent(
                    extraValue = "Purple",
                    secondExtraValue = selectedColor
                )
            }
        }

        fNote5.setOnClickListener {
            configureNotes(selectedImage = 3).also {
                sendBroadCastIntent(
                    extraValue = "Green",
                    secondExtraValue = selectedColor
                )
            }
        }

        fNote6.setOnClickListener {
            configureNotes(selectedImage = 4).also {
                sendBroadCastIntent(
                    extraValue = "Orange",
                    secondExtraValue = selectedColor
                )
            }
        }

        fNote7.setOnClickListener {
            configureNotes(selectedImage = 5).also {
                sendBroadCastIntent(
                    extraValue = "Black",
                    secondExtraValue = selectedColor
                )
            }
        }

        // Bottom Option Listener
        layoutImage.setOnClickListener {
            sendBroadCastIntent(extraValue = ACTION_IMAGE).also { dismiss() }
        }

        layoutWebUrl.setOnClickListener {
            sendBroadCastIntent(extraValue = ACTION_WEB_URL).also { dismiss() }
        }

        layoutHandWriting.setOnClickListener {
            sendBroadCastIntent(extraValue = ACTION_HAND_WRITING).also { dismiss() }
        }

        layoutDeleteNote.setOnClickListener {
            sendBroadCastIntent(extraValue = ACTION_DELETE).also { dismiss() }
        }
    }

    @SuppressLint("ResourceType")
    private fun BottomSheetNotesOptionsBinding.configureNotes(selectedImage: Int) {
        
        val imageNoteList = buildList {
            add(ImageNoteSetup(imgNote1, resources.getString(ColorBlueNote)))
            add(ImageNoteSetup(imgNote2, resources.getString(ColorYellowNote)))
            add(ImageNoteSetup(imgNote3, resources.getString(ColorRedNote)))
            add(ImageNoteSetup(imgNote4, resources.getString(ColorWhiteNote)))
            add(ImageNoteSetup(imgNote5, resources.getString(ColorOrangeNote)))
            add(ImageNoteSetup(imgNote6, resources.getString(ColorBlackNote)))
        }

        selectedColor = imageNoteList[selectedImage].color
        imageNoteList.onEachIndexed { currentImage, imageNoteSetup ->
            imageNoteSetup.imageView.setImageResource(
                if (currentImage == selectedImage) R.drawable.icon_tick_24px
                else 0
            )
        }
    }

    private fun sendBroadCastIntent(extraValue: String, secondExtraValue: String? = null) {
        val intent = Intent("bottom_sheet_action")
        intent.putExtra("action",extraValue)
        intent.putExtra("selectedColor", secondExtraValue).takeIf { secondExtraValue != null }
        LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
    }

    data class ImageNoteSetup(
        val imageView: ImageView,
        val color: String
    )

    companion object {
        var existingNotes = false

        fun createInstance(isExistingNotes: Boolean = false) = NoteBottomSheet().apply {
            existingNotes = isExistingNotes
        }
    }
}