package com.example.healthcareapp.data.models

import com.example.healthcareapp.utils.Formater
import java.util.Date


data class MeditationHistory (
    var createdAt: String = "",
    var name: String = "",
    var uri: String = "",
    var type: String = "",
    var duration: Long = 0,
    var imageUri: String = "",
)
{
    fun fromMeditationModel(meditationModel: MeditationModel) {
        createdAt  = Formater.formatChatTime(Date())
        name = meditationModel.name
        uri = meditationModel.uri
        type = meditationModel.type
        duration = 0
        imageUri = meditationModel.imageUri
    }
}