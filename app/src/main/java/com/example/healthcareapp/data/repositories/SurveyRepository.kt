package com.example.healthcareapp.data.repositories

import android.util.Log
import com.example.healthcareapp.data.datasource.ModelApiImp
import com.example.healthcareapp.data.models.ChatModel
import com.example.healthcareapp.data.models.MessageModel
import com.example.healthcareapp.data.models.QuestionModel
import com.example.healthcareapp.data.models.QuestionnaireModel
import com.example.healthcareapp.data.network.ModelApi
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.http.GET
import java.util.Date

class SurveyRepository {
    private val questionnaireRef = FirebaseFirestore.getInstance().collection("questionnaire")
    private val questionRef = FirebaseFirestore.getInstance().collection("question")
    private val db = FirebaseFirestore.getInstance()
    private val modelApi =  ModelApiImp();
    companion object{
        var instance: SurveyRepository? = null
            get() {
                if (field == null) {
                    field = SurveyRepository()
                }
                return field
            }
            private set
    }
    fun getQuestions(onSuccess: (questionList: List<QuestionModel>) -> Unit , onFailure:() ->Unit){
        questionRef
            .get()
            .addOnSuccessListener { querySnapshot ->
            if (!querySnapshot.isEmpty) {
                val questionList: MutableList<QuestionModel> = mutableListOf();
                for (document in querySnapshot) {
                    val question = document.toObject(QuestionModel::class.java)
                    question.id = document.id
                    question.isSelectAnswer = document.getBoolean("isSelectAnswer")?:false;
                    questionList.add(question);
                }
                onSuccess(questionList) // Call onSuccess with the loaded message list
                Log.i("QUESTION", " retrieved question document successfully ${questionList.size}")
            }
        }
            .addOnFailureListener { e ->
                Log.e("QUESTION", "Error retrieving question document", e)
                onFailure();
            }
    }
//    snoring: String,
//    bmi: Float,
//    sleepHours: Float,
//    meal: Int,
//    exercise: String,
//    studyHours: Float,
//    sleepProb: String,
//    age: Int,
//    smoke: String,
    private suspend fun getPredictedResult(
    snoring: String,
    bmi: String,
    sleepHours: String,
    meal: String,
    exercise: String,
    studyHours: String,
    sleepProb: String,
    age: String,
    smoke: String,
    ): Float {
        val response = modelApi.getPredictedValue(
            snoring,
            bmi ,
            sleepHours ,
            meal,
            exercise,
            studyHours ,
            sleepProb ,
            age ,
            smoke ,
        )
        val predictedValue = response.prediction[0][0]
        Log.d("PredictedValue", "Predicted value: $predictedValue")
        return predictedValue;
    }

    fun uploadQuestionnaireResult(userId:String, questionResult: List<QuestionModel>) {

        GlobalScope.launch {

            val userAnswers = questionResult.map { questionModel -> questionModel.userAnswer }.toTypedArray()

            val predictedResult = getPredictedResult(userAnswers[0],
                userAnswers[1],
                userAnswers[2],
                userAnswers[3],
                userAnswers[4],
                userAnswers[5],
                userAnswers[6],
                userAnswers[7],
                userAnswers[8],)
            val newQuestionnaireRecord = QuestionnaireModel(userId = userId);
            newQuestionnaireRecord.predictedResult = predictedResult
            newQuestionnaireRecord.questionResult = questionResult.map { questionModel ->
                mapOf(
                    ("questionId" to questionModel.id),
                    ("userAnswer" to questionModel.userAnswer)
                )
            }.toMutableList()
            questionnaireRef.add(newQuestionnaireRecord)
                .addOnSuccessListener {
                    Log.i("QUESTION", "Adding questionnaire successfully")
                }
                .addOnFailureListener { e ->
                    Log.e("QUESTION", "Err while adding questionnaire ${e}")
                }
        }
    }
}