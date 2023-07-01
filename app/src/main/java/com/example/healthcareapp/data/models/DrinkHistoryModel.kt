package com.example.healthcareapp.data.models

import com.google.firebase.Timestamp

data class DrinkHistoryModel (
    var createdAt: Timestamp = Timestamp.now(),
    var value: Double = 0.0,
    var maxValue: Double = 0.0
)