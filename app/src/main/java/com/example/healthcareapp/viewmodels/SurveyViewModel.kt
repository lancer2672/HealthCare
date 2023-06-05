package com.example.healthcareapp.viewmodels

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.healthcareapp.data.models.QuestionModel
import com.example.healthcareapp.data.repositories.SurveyRepository
import com.google.firebase.auth.FirebaseAuth

class SurveyViewModel: ViewModel() {
    val questions: MutableList<QuestionModel> = mutableListOf()
    val isLoading = MutableLiveData(false);
    val textViewQuestionContent = ObservableField<String>("");
    var answerList = mutableListOf<String>()
    var currentQuestionIndex =  MutableLiveData<Int>(-1);
    var selectedIndex = MutableLiveData<Int>(-1);
    fun loadQuestionList() {
        isLoading.value = true;
        SurveyRepository.instance?.getQuestions(onSuccess = {result->
            questions.clear();
            questions.addAll(result);
            isLoading.value = false;
        }, onFailure = {
            isLoading.value = false;
        })
    }
    fun loadCurrentQuestionAndAnswers(){
       if(currentQuestionIndex.value != -1 && currentQuestionIndex.value!! < questions.size){
           val currentQuestion = questions[currentQuestionIndex.value!!]
           textViewQuestionContent.set(currentQuestion.content)
           answerList = currentQuestion.answerType.map {
                it.value.toString();
           }.toMutableList()
       }
    }
    fun moveToNextQuestion(){0
        if(currentQuestionIndex.value!! >= 0){
            saveAnswer()
        }
    }
    private fun saveAnswer(){
        if(selectedIndex.value != -1){
            //save user answer to an item in questions
            questions[currentQuestionIndex.value!!].userAnswer = answerList[selectedIndex.value!!];
            //moveToNextQuestion
            currentQuestionIndex.value = currentQuestionIndex.value!! + 1;
            //reset to unselected
            selectedIndex.value = -1;
        }
    }
    fun uploadQuestionnaireResult(){
        SurveyRepository.instance
            ?.uploadQuestionnaireResult(FirebaseAuth.getInstance().currentUser?.uid.toString(),questions)
    }
}