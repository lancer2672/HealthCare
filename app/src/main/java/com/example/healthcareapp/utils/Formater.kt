package com.example.healthcareapp.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Formater {
    companion object{
        fun formatChatTime(date:Date): String{
            val sdf = SimpleDateFormat("dd/M/yyyy hh:mm", Locale.getDefault())
            return sdf.format(date);
        }
        fun formatPredictedValue(value: Float): String {
            val advice = when {
                value < 0.1 -> "🎉 Tuyệt vời!Bạn không có khả năng bị đau đầu."
                value < 0.2 -> "😊 Tốt! Khả năng bị đau đầu rất thấp."
                value < 0.4 -> "🙂 Khá tốt! Sức khoẻ của bạn khá tốt và khả năng bị đau đầu thấp."
                value < 0.6 -> "😐 Khả năng bị đau đầu ở mức trung bình."
                value < 0.8 -> "🙁 Khả năng bị đau đầu hơi cao."
                value < 0.9 -> "😞 Kém! Khả năng bị đau đầu cao."
                else -> "😔 Rất kém! Bạn có khả năng cao bị đau đầu."
            }

            return advice
        }
        fun formatDuration(durationMs: Long): String {
            val seconds = (durationMs / 1000).toInt()
            val minutes = seconds / 60
            val remainingSeconds = seconds % 60
            return if (minutes > 0) {
                String.format("%d:%02d", minutes, remainingSeconds)
            } else {
                String.format("0:%02d", remainingSeconds)
            }
        }
        fun formatTime(hour: Int, minute: Int): String {
            return String.format("%02d:%02d", hour, minute)
        }
    }
}