package com.example.healthcareapp.data.models

import java.util.Date

data class QuestionnaireModel (
    val userId: String = "",
    val createdAt: Date = Date(),
    var questionResult: MutableList<Map<String,String>> = mutableListOf(),
    val predictedResult: Float = 0f
)