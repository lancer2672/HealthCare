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
    }
}