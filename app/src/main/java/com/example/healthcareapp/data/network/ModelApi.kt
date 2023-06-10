package com.example.healthcareapp.data.network

import com.example.healthcareapp.data.models.PredictedValueResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ModelApi {
    @GET("get-predicted-values")
    suspend fun getPredictedValue(
        @Query("Snoring") snoring: String,
        @Query("BMI") bmi: String,
        @Query("sleep_hrs") sleepHours: String,
        @Query("Meal") meal: String,
        @Query("Exercise") exercise: String,
        @Query("studyhrs") studyHours: String,
        @Query("Sleep_prob") sleepProb: String,
        @Query("Age") age: String,
        @Query("Smoke") smoke: String,
    ): PredictedValueResponse
}