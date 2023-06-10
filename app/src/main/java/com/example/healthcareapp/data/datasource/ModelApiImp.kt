package com.example.healthcareapp.data.datasource

import com.example.healthcareapp.data.network.ModelApi
import com.example.healthcareapp.data.models.PredictedValueResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ModelApiImp: ModelApi {
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.122.4:5000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service = retrofit.create(ModelApi::class.java)
    override suspend fun getPredictedValue(
        snoring: String,
        bmi: String,
        sleepHours: String,
        meal: String,
        exercise: String,
        studyHours: String,
        sleepProb: String,
        age: String,
        smoke: String,
    ): PredictedValueResponse {
            return service.getPredictedValue(
                snoring,  bmi, sleepHours, meal,exercise,  studyHours, sleepProb,age, smoke,
            )
    }
}