package com.example.healthcareapp.ui.custom_components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.example.healthcareapp.R

class CircleResultComponent @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val circle: Circle

    init {
        orientation = HORIZONTAL
        LayoutInflater.from(context).inflate(R.layout.circle_result_component, this, true)

        circle = findViewById(R.id.circle)
    }

    fun setValue(value: Float) {
        circle.value = value
    }
}
