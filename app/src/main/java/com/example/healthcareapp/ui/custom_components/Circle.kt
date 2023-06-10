package com.example.healthcareapp.ui.custom_components

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.ArcShape
import android.graphics.drawable.shapes.RoundRectShape
import android.util.AttributeSet
import android.view.View


class Circle(context: Context?, attrs: AttributeSet?) :
    View(context, attrs) {
    private val paint: Paint
    private val rect: RectF
    var angle: Float = 0f

    init {
        val strokeWidth = 40
        paint = Paint()
        paint.isAntiAlias = true
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = strokeWidth.toFloat()
        //Circle color
        paint.color = Color.argb(0.8f,131f,73f,56f)
        //size 200x200 example
        rect = RectF(
            strokeWidth.toFloat(),
            strokeWidth.toFloat(),
            (200 + strokeWidth).toFloat(),
            (200 + strokeWidth).toFloat()
        )
        //Initial Angle (optional, it can be zero)
        angle = 0f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawArc(rect, START_ANGLE_POINT.toFloat(), angle, false, paint)
    }

    companion object {
        private const val START_ANGLE_POINT = 90
    }
}