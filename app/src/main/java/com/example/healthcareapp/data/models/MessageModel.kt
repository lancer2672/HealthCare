package com.example.healthcareapp.data.models

data class MessageModel (
    var message: String = "",
    var isBotMessage: Boolean = false,
    var createdAt: String = "",
)