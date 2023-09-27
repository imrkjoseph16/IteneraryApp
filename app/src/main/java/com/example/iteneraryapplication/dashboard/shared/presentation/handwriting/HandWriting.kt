package com.example.iteneraryapplication.dashboard.shared.presentation.handwriting

import android.annotation.SuppressLint
import android.provider.MediaStore.Images.Media.insertImage
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import com.example.iteneraryapplication.R
import com.example.iteneraryapplication.app.extension.setVisible
import com.example.iteneraryapplication.app.foundation.BaseActivity
import com.example.iteneraryapplication.app.shared.component.HandDrawingView
import com.example.iteneraryapplication.app.util.Default.Companion.IMAGE_FILE_DESCRIPTION
import com.example.iteneraryapplication.app.util.Default.Companion.IMAGE_FILE_PNG_FORMAT
import com.example.iteneraryapplication.app.util.Default.Companion.IMAGE_FILE_SAVE_SUCCESS
import com.example.iteneraryapplication.app.util.Default.Companion.SOMETHING_WENT_WRONG
import com.example.iteneraryapplication.app.util.Default.Companion.getRandomUUID
import com.example.iteneraryapplication.app.util.showToastMessage
import com.example.iteneraryapplication.dashboard.shared.presentation.handwriting.HandWriting.DrawingStateEnum.*
import com.example.iteneraryapplication.databinding.ActivityHandWritingBinding

class HandWriting : BaseActivity<ActivityHandWritingBinding>() {

    private var currentPaint: ImageButton? = null

    override val inflater: (LayoutInflater) -> ActivityHandWritingBinding
        get() = ActivityHandWritingBinding::inflate

    override fun onActivityCreated() {
        super.onActivityCreated()
        binding.apply {
           configureViews()
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun ActivityHandWritingBinding.configureViews() {
        currentPaint = paintColors.getChildAt(0) as ImageButton?
        currentPaint?.setImageDrawable(resources.getDrawable(R.drawable.paint_pressed))

        back.setOnClickListener {
            finish()
        }

        toolsIcon.setOnClickListener {
            modifyToolsIcon()
        }

        newDraw.setOnClickListener {
            handleDrawingState(drawingState = NEW_DRAW)
        }

        drawing.setOnClickListener {
            handleDrawingState(drawingState = DRAW)
        }

        erase.setOnClickListener {
            handleDrawingState(drawingState = ERASE)
        }

        saveDrawing.setOnClickListener {
            handleDrawingState(drawingState = DOWNLOAD)
        }

        proceedDrawing.setOnClickListener {
            finish()
        }
    }

    private fun ActivityHandWritingBinding.modifyToolsIcon() {
        layoutTools.apply {
            if (isShown) {
                setVisible(false).also { toolsIcon.setImageResource(R.drawable.icon_double_arrow_up_24px) }
            } else {
                setVisible(true).also { toolsIcon.setImageResource(R.drawable.icon_double_arrow_down_24px) }
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun onPaintClicked(view: View) {
        binding.apply {
            if (view != currentPaint) {
                val imgView = view as ImageButton
                val color: String = view.getTag().toString()
                drawingView.setColor(color)
                imgView.setImageDrawable(resources.getDrawable(R.drawable.paint_pressed))
                currentPaint?.setImageDrawable(resources.getDrawable(R.drawable.icon_paint))
                currentPaint = view
            }
        }
    }

    private fun ActivityHandWritingBinding.handleDrawingState(
        drawingState: DrawingStateEnum
    ) = when(drawingState) {
        DRAW -> drawingView.setupDrawing()
        NEW_DRAW -> drawingView.startNew()
        DOWNLOAD -> drawingView.downloadDrawImage()
        ERASE -> drawingView.eraseDrawing()
    }

    private fun HandDrawingView.eraseDrawing() = setErase(true).also { setBrushSize(getLastBrushSize()) }

    private fun HandDrawingView.downloadDrawImage() {
        isDrawingCacheEnabled = true
        val imgSaved: String? = insertImage(
            contentResolver, drawingCache,
        getRandomUUID() +
            IMAGE_FILE_PNG_FORMAT,
            IMAGE_FILE_DESCRIPTION
        )
        destroyDrawingCache()
        if (imgSaved != null) showToastMessage(this@HandWriting, IMAGE_FILE_SAVE_SUCCESS)
        else showToastMessage(this@HandWriting, SOMETHING_WENT_WRONG)
    }

    enum class DrawingStateEnum {
        DRAW,
        NEW_DRAW,
        ERASE,
        DOWNLOAD
    }
}