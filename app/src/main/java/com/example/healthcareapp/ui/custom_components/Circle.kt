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
     val paint: Paint
     val rect: RectF
     var angle:Float = 0f
    var value: Float = 0f
        set(value) {
            field = value
            updateColor()
            updateAngle()
            invalidate()
        }

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
            (130 + strokeWidth).toFloat(),
            (130 + strokeWidth).toFloat()
        )
    }

    private fun updateColor() {
        val hue = when {
            value <= 0.5 -> lerp(120f, 60f, value * 2)
            else -> lerp(60f, 0f, (value - 0.5f) * 2)
        }
        val color = Color.HSVToColor(floatArrayOf(hue, 1f, 1f))
        paint.color = color
    }
    fun triggerAnimation() {
        updateAngle()
    }
    private fun updateAngle() {
        angle = value * 360f
        val anim = CircleAngleAnimation(this, angle.toInt())
        anim.duration = 1000
        startAnimation(anim)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawArc(rect, START_ANGLE_POINT.toFloat(), angle, false, paint)
    }

    companion object {
        private const val START_ANGLE_POINT = -90

        private fun lerp(a: Float, b: Float, t: Float): Float {
            return a + (b - a) * t
        }
    }
}