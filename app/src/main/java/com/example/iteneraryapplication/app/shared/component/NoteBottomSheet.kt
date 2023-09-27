package com.example.iteneraryapplication.app.shared.component

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.iteneraryapplication.R
import com.example.iteneraryapplication.app.extension.setVisible
import com.example.iteneraryapplication.app.foundation.BaseBottomSheetFragment
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
            sendBroadCastIntent(extraValue = "Image").also { dismiss() }
        }

        layoutWebUrl.setOnClickListener {
            sendBroadCastIntent(extraValue = "WebUrl").also { dismiss() }
        }

        layoutDeleteNote.setOnClickListener {
            sendBroadCastIntent(extraValue = "DeleteNote").also { dismiss() }
        }
    }

    private fun BottomSheetNotesOptionsBinding.configureNotes(selectedImage: Int) {

        val imageNoteList = buildList {
            add(ImageNoteSetup(imgNote1, "#404FF6"))
            add(ImageNoteSetup(imgNote2, "#F4C057"))
            add(ImageNoteSetup(imgNote3, "#EB5849"))
            add(ImageNoteSetup(imgNote4, "#0aebaf"))
            add(ImageNoteSetup(imgNote5, "#ff7746"))
            add(ImageNoteSetup(imgNote6, "#333333"))
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