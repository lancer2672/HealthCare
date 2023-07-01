package com.example.healthcareapp.data.models

data class User(
    var userId: String = "",
    var displayName: String = "",
    var gender: String = "",
    var age: String = "",
    var avatar: String = "",
    var meditationHistory: ArrayList<MeditationHistory> = arrayListOf(),
    var drinkHistory: ArrayList<DrinkHistoryModel> = arrayListOf()
)
