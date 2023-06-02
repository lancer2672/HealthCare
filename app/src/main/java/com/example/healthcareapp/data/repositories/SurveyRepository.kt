package com.example.healthcareapp.data.repositories

import android.util.Log
import com.example.healthcareapp.data.models.ChatModel
import com.example.healthcareapp.data.models.MessageModel
import com.example.healthcareapp.data.models.QuestionModel
import com.example.healthcareapp.data.models.QuestionnaireModel
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Date

class SurveyRepository {
    private val questionnaireRef = FirebaseFirestore.getInstance().collection("questionnaire")
    private val questionRef = FirebaseFirestore.getInstance().collection("question")
    private val db = FirebaseFirestore.getInstance()
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
    fun uploadQuestionnaireResult(userId:String, questionResult: List<QuestionModel>){
        val newQuestionnaireRecord = QuestionnaireModel(userId = userId);
        newQuestionnaireRecord.questionResult = questionResult.map { questionModel->
            mapOf(("questionId" to questionModel.id),("userAnswer" to questionModel.userAnswer))
        }.toMutableList()
        questionnaireRef.add(newQuestionnaireRecord)
            .addOnSuccessListener {
                Log.i("QUESTION", "Adding questionnaire successfully")
            }
            .addOnFailureListener{e->
                Log.e("QUESTION", "Err while adding questionnaire ${e}", )
            }
    }
}