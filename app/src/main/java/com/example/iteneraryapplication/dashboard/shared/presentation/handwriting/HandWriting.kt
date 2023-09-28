package com.example.iteneraryapplication.dashboard.shared.presentation.handwriting

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import com.example.iteneraryapplication.R
import com.example.iteneraryapplication.app.extension.setVisible
import com.example.iteneraryapplication.app.foundation.BaseActivity
import com.example.iteneraryapplication.app.widget.HandDrawingView
import com.example.iteneraryapplication.app.util.Default.Companion.IMAGE_FILE_DESCRIPTION
import com.example.iteneraryapplication.app.util.Default.Companion.IMAGE_FILE_SAVE_SUCCESS
import com.example.iteneraryapplication.app.util.Default.Companion.REQUEST_CODE_GET_DRAWING
import com.example.iteneraryapplication.app.util.Default.Companion.SOMETHING_WENT_WRONG
import com.example.iteneraryapplication.app.util.showToastMessage
import com.example.iteneraryapplication.dashboard.shared.presentation.handwriting.HandWriting.DrawingStateEnum.*
import com.example.iteneraryapplication.databinding.ActivityHandWritingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
            handleDrawingState(selectedTool = it, drawingState = NEW_DRAW)
        }

        drawing.setOnClickListener {
            handleDrawingState(selectedTool = it, drawingState = DRAW)
        }

        erase.setOnClickListener {
            handleDrawingState(selectedTool = it, drawingState = ERASE)
        }

        saveDrawing.setOnClickListener {
            handleDrawingState(selectedTool = it, drawingState = DOWNLOAD)
        }

        proceedDrawing.setOnClickListener {
            drawingView.passDrawingUri().also { finish() }
        }
    }

    private fun HandDrawingView.passDrawingUri() {
        setResult(
            REQUEST_CODE_GET_DRAWING, Intent().apply {
                putExtra(
                    IMAGE_FILE_DESCRIPTION,
                    permissionUtil.getImageUri(
                    context = this@HandWriting,
                    bitmap = this@passDrawingUri).toString()
                )
            }
        )
    }

    private fun ActivityHandWritingBinding.modifyToolsIcon() {
        layoutTools.apply {
            if (isShown) {
                setVisible(false).also { toolsIcon.setImageResource(R.drawable.icon_double_arrow_down_24px) }
            } else {
                setVisible(true).also { toolsIcon.setImageResource(R.drawable.icon_double_arrow_up_24px) }
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
        selectedTool: View,
        drawingState: DrawingStateEnum
    ) {
        arrayOf(newDraw, drawing, erase, saveDrawing).onEachIndexed { _, uiImageView ->
            uiImageView.apply {
                if (uiImageView == selectedTool) setBackgroundResource(R.drawable.background_selected)
                else setBackgroundResource(android.R.color.transparent)
            }
        }

        when (drawingState) {
            DRAW -> drawingView.setupDrawing()
            NEW_DRAW -> drawingView.startNew()
            DOWNLOAD -> drawingView.downloadDrawImage()
            ERASE -> drawingView.eraseDrawing()
        }
    }

    private fun HandDrawingView.eraseDrawing() = setErase(true).also { setBrushSize(getLastBrushSize()) }

    private fun HandDrawingView.downloadDrawImage() {
        permissionUtil.getImageUri(
            context = this@HandWriting,
            bitmap = this@downloadDrawImage
        ).also {
            if (it != null) showToastMessage(this@HandWriting, IMAGE_FILE_SAVE_SUCCESS)
            else showToastMessage(this@HandWriting, SOMETHING_WENT_WRONG)
        }
    }

    enum class DrawingStateEnum {
        DRAW,
        NEW_DRAW,
        ERASE,
        DOWNLOAD
    }
}