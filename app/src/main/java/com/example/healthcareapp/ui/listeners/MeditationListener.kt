package com.example.healthcareapp.ui.listeners

import com.example.healthcareapp.data.models.MeditationModel

interface MeditationListener {
    fun startSession(meditationModel: MeditationModel);
}