package com.example.iteneraryapplication.app.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.example.iteneraryapplication.R

class HandDrawingView(
    context: Context?, attrs: AttributeSet?
) : View(context, attrs) {

    // Drawing Path
    private var drawPath: Path? = null

    // Drawing and Canvas Paint
    private var drawPaint: Paint? = null
    private var canvasPaint: Paint? = null

    // Initial Color
    private var paintColor = -0x9a0000

    // Canvas
    private var drawCanvas: Canvas? = null

    // Canvas Bitmap
    private var canvasBitmap: Bitmap? = null
    private var brushSize = 0f
    private var lastBrushSize = 0f

    init {
        setupDrawing()
    }

    fun startNew() = setDrawColorBackground().also { invalidate() }

    fun setErase() {
        drawPaint?.color = context.getColor(R.color.colorLightBeige)
    }

    fun getLastBrushSize() = lastBrushSize

    fun setBrushSize(newSize: Float) {
        brushSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            newSize, resources.displayMetrics
        ).also { size ->
            drawPaint?.strokeWidth = size
        }
    }

    private fun setDrawColorBackground() = drawCanvas?.drawColor(ContextCompat.getColor(context, R.color.colorLightBeige))

    override fun onSizeChanged(width: Int, height: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(width, height, oldw, oldh)
        canvasBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        drawCanvas = Canvas(canvasBitmap!!)
        setDrawColorBackground()
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawBitmap(canvasBitmap!!, 0f, 0f, canvasPaint)
        canvas.drawPath(drawPath!!, drawPaint!!)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(touch: MotionEvent): Boolean {
        when (touch.action) {
            MotionEvent.ACTION_DOWN -> drawPath?.moveTo(touch.x, touch.y)
            MotionEvent.ACTION_MOVE -> drawPath?.lineTo(touch.x, touch.y)
            MotionEvent.ACTION_UP -> drawCanvas?.drawPath(drawPath!!, drawPaint!!).also { drawPath?.reset() }
            else -> return false
        }
        invalidate()
        return true
    }

    fun setColor(newColor: String?) {
        invalidate()
        paintColor = Color.parseColor(newColor)
        drawPaint?.color = paintColor
    }

    fun setupDrawing() {
        drawPath = Path()
        canvasPaint = Paint(Paint.DITHER_FLAG)
        brushSize = resources.getInteger(R.integer.small_size).toFloat()
        lastBrushSize = brushSize
        drawPaint = Paint().apply {
            strokeWidth = brushSize
            color = paintColor
            isAntiAlias = true
            strokeWidth = 5f
            style = Paint.Style.STROKE
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
        }
    }
}