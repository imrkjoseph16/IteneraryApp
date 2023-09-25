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

    private var selectedColor = "#171C26"

    override fun onBottomSheetCreated() {
        super.onBottomSheetCreated()
        binding.apply { 
            configureViews()
        }
    }
    
    private fun BottomSheetNotesOptionsBinding.configureViews() {
        layoutDeleteNote.setVisible(noteId != -1)

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
            add(ImageNoteSetup(imgNote1, "#4e33ff"))
            add(ImageNoteSetup(imgNote2, "#ffd633"))
            add(ImageNoteSetup(imgNote3, "#ae3b76"))
            add(ImageNoteSetup(imgNote4, "#0aebaf"))
            add(ImageNoteSetup(imgNote5, "#ff7746"))
            add(ImageNoteSetup(imgNote6, "#202734"))
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
        var noteId = -1
        
        fun createInstance(id: Int = noteId): NoteBottomSheet{
            val args = Bundle()
            val fragment = NoteBottomSheet()
            fragment.arguments = args
            noteId = id
            return fragment
        }
    }
}