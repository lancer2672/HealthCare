package com.example.healthcareapp.data.models

data class QuestionModel (
    var id: String = "",
    var content: String = "",
    var answerType:Map<String, Any> = mapOf(),
    var userAnswer:String = "",
    var isSelectAnswer: Boolean = false

)