package com.example.healthcareapp.data.models

data class ChatModel (
    var userId: String = "",
    var messages: List<MessageModel> = mutableListOf()
)