package com.example.healthcareapp.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Formater {
    companion object{
        fun formatChatTime(date:Date): String{
            val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss", Locale.getDefault())
            return sdf.format(date);
        }
        fun formatPredictedValue(value: Float): String {
            val values = arrayOf(
                "Bạn không có khả năng bị đau đầu",
                "Khả năng bị đau đầu của bạn rất thấp",
                "Khả năng bị đau đầu của bạn thấp",
                "Khả năng bị đau đầu của bạn ở mức trung bình",
                "Khả năng bị đau đầu của bạn hơi cao",
                "Khả năng bị đau đầu của bạn cao",
                "Bạn có khả năng bị đau đầu"
            )
            val positions = floatArrayOf(0f, 0.15f, 0.3f, 0.5f, 0.7f, 0.85f, 1f)

            var index = -1
            var minDistance = Float.MAX_VALUE
            for (i in positions.indices) {
                val distance = kotlin.math.abs(value - positions[i])
                if (distance < minDistance) {
                    minDistance = distance
                    index = i
                }
            }
            // Make sure the index is within the bounds of our values array
            return if (index < 0 || index >= values.size) {
                ""
            } else values[index]
        }

    }
}