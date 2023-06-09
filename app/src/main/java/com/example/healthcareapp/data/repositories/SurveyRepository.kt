package com.example.healthcareapp.data.repositories

import android.util.Log
import com.example.healthcareapp.data.datasource.ModelApiImp
import com.example.healthcareapp.data.models.ChatModel
import com.example.healthcareapp.data.models.MessageModel
import com.example.healthcareapp.data.models.QuestionModel
import com.example.healthcareapp.data.models.QuestionnaireModel
import com.example.healthcareapp.data.network.ModelApi
import com.example.healthcareapp.utils.DateUtils
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import retrofit2.http.GET
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.Date

class SurveyRepository {
    private val questionnaireRef = FirebaseFirestore.getInstance().collection("questionnaire")
    private val questionRef = FirebaseFirestore.getInstance().collection("question")
    private val db = FirebaseFirestore.getInstance()
//    val storageRef = Firebase.storage.reference
//    val fileRef = storageRef.child("data_set/Students.xlsx")
//    val localFile = File.createTempFile("temp", "xlsx")
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
    fun getQuestions( onSuccess: (questionList: List<QuestionModel>) -> Unit , onFailure:() ->Unit){
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
    fun getSurveyHistory(
        userId: String,
        onSuccess: (surveyList: List<QuestionnaireModel>) -> Unit,
        onFailure: () -> Unit
    ) {
//        .whereEqualTo("userId", userId)
        questionnaireRef
            .orderBy("createdAt")
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val surveyList: MutableList<QuestionnaireModel> = ArrayList()
                    for (document in querySnapshot) {
                        val survey = document.toObject(QuestionnaireModel::class.java)
                        surveyList.add(survey);
                    }
                    onSuccess(surveyList) // Call onSuccess with the loaded survey list
                    Log.i("SURVEY", "Retrieved survey documents successfully: ${surveyList.size}")
                }
            }
            .addOnFailureListener { e ->
                Log.e("SURVEY", "Error retrieving survey documents", e)
                onFailure()
            }
    }
    private suspend fun getPredictedResult(
        snoring: String,
        bmi: String,
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
    fun uploadQuestionnaireResult(userId:String, questionResult: List<QuestionModel>, onSuccess: (Float) -> Unit) {
        runBlocking  {
            val userAnswers = questionResult.map { questionModel -> questionModel.userAnswer }.toTypedArray()
            val predictedResult = getPredictedResult(userAnswers[0],
                userAnswers[1],
                userAnswers[2],
                userAnswers[3],
                userAnswers[4],
                userAnswers[5],
                userAnswers[6],
                userAnswers[7],
             )
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
                    onSuccess.invoke(predictedResult);
                }
                .addOnFailureListener { e ->
                    Log.e("QUESTION", "Err while adding questionnaire ${e}")
                }
        }
    }

}