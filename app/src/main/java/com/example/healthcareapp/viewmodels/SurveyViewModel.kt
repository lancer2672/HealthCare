package com.example.healthcareapp.viewmodels

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.healthcareapp.data.models.QuestionModel
import com.example.healthcareapp.data.repositories.SurveyRepository
import com.google.firebase.auth.FirebaseAuth

class SurveyViewModel: ViewModel() {
    val questions: MutableList<QuestionModel> = mutableListOf()
    val isLoading = MutableLiveData(false);
    val isSelectAnswer = MutableLiveData(false);
    val userFillInAnswer = ObservableField<String>("");
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
    fun  loadCurrentQuestionAndAnswers(){
       if(currentQuestionIndex.value != -1 && currentQuestionIndex.value!! < questions.size){
           val currentQuestion = questions[currentQuestionIndex.value!!]
           textViewQuestionContent.set(currentQuestion.content)
           isSelectAnswer.value = currentQuestion.isSelectAnswer
           if(currentQuestion.isSelectAnswer){
               answerList = currentQuestion.answerType.map {answerMap->
                   answerMap.value.toString();
               }.toMutableList()
               Log.d("SurveyActivity AnswerList",answerList.size.toString())
           }
       }
    }
    fun moveToNextQuestion(){
        if(currentQuestionIndex.value!! >= 0 ){
            saveAnswer()
        }
    }
    fun moveToPrevQuestion(){
        if(currentQuestionIndex.value!! >0){
           currentQuestionIndex.value = currentQuestionIndex.value!! -1;
        }
    }
    private fun saveAnswer(){
        //answer with choices
        if(isSelectAnswer.value == true){
            if(selectedIndex.value != -1){
                //save user answer to an item in questions
                questions[currentQuestionIndex.value!!].userAnswer = answerList[selectedIndex.value!!];
                //reset to unselected
                selectedIndex.value = -1;
                //moveToNextQuestion
                currentQuestionIndex.value = currentQuestionIndex.value!! + 1;
            }
        }
        //answer by filling in
        if(!userFillInAnswer.get().isNullOrEmpty()){
                questions[currentQuestionIndex.value!!].userAnswer = userFillInAnswer.get()!!
                userFillInAnswer.set("");
            //moveToNextQuestion
            currentQuestionIndex.value = currentQuestionIndex.value!! + 1;
        }

    }
    fun uploadQuestionnaireResult(){
        SurveyRepository.instance
            ?.uploadQuestionnaireResult(FirebaseAuth.getInstance().currentUser?.uid.toString(),questions)
    }
}