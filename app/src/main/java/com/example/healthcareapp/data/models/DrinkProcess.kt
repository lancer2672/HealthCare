package com.example.healthcareapp.data.models

import com.google.firebase.Timestamp

data class DrinkProcess(
    var date:Timestamp = Timestamp.now(),
    var value: Double = 0.0,
    var maxValue: Double = 0.0,
    var userId: String = ""
)