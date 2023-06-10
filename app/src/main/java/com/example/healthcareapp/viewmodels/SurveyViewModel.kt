package com.example.healthcareapp.viewmodels

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.healthcareapp.data.models.MonthYearModel
import com.example.healthcareapp.data.models.QuestionModel
import com.example.healthcareapp.data.models.QuestionnaireModel
import com.example.healthcareapp.data.repositories.SurveyRepository
import com.github.mikephil.charting.data.BarEntry
import com.google.firebase.auth.FirebaseAuth
import java.util.Calendar

class SurveyViewModel: ViewModel() {
    val questions: MutableList<QuestionModel> = mutableListOf()
    val surveyHistoryList: MutableList<QuestionnaireModel> = mutableListOf()
    val monthYearList: MutableList<MonthYearModel> = mutableListOf()
    val isLoading = MutableLiveData(false);
    val isSelectAnswer = MutableLiveData(false);
    val userFillInAnswer = ObservableField<String>("");
    val textViewQuestionContent = ObservableField<String>("");
    var answerList = mutableListOf<String>()
    var currentQuestionIndex =  MutableLiveData<Int>(-1);
    var selectedIndex = MutableLiveData<Int>(-1);
    var dataChart: ArrayList<BarEntry> = arrayListOf()
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
    fun getSurveyHistory(){
        isLoading.value = true;
        SurveyRepository.instance?.getSurveyHistory(onSuccess = {result->
            surveyHistoryList.clear();
            surveyHistoryList.addAll(result);

            //get range of month year
            monthYearList.clear()
            monthYearList.addAll(createTimeFrameBySurveyHistoryList(result))

            isLoading.value = false;

        }, onFailure = {
            isLoading.value = false;
        })
    }
    fun getDataChartByDate(monthYearModel: MonthYearModel) {
        dataChart.clear();
        surveyHistoryList.forEach { questionnaire ->
            val calendar = Calendar.getInstance()
            calendar.time = questionnaire.createdAt
            val questionnaireMonth = calendar.get(Calendar.MONTH) + 1 // Adjust for 0-based indexing
            val questionnaireYear = calendar.get(Calendar.YEAR)
            if(questionnaireMonth == monthYearModel.month && questionnaireYear == monthYearModel.year)
            {
                dataChart.add(BarEntry(monthYearModel.month.toFloat(),questionnaire.predictedResult))
            }

        }
    }
    private fun createTimeFrameBySurveyHistoryList(list: List<QuestionnaireModel>): MutableList<MonthYearModel> {
        val monthYearList = mutableListOf<MonthYearModel>()
        if (list.isNotEmpty()) {
            val firstItem = list.first()
            val lastItem = list.last()

            val calendarStart = Calendar.getInstance()
            calendarStart.time = firstItem.createdAt
            val startMonth = calendarStart.get(Calendar.MONTH) + 1
            val startYear = calendarStart.get(Calendar.YEAR)

            val calendarEnd = Calendar.getInstance()
            calendarEnd.time = lastItem.createdAt
            val endMonth = calendarEnd.get(Calendar.MONTH) + 1
            val endYear = calendarEnd.get(Calendar.YEAR)

            for (year in startYear..endYear) {
                val startMonthOfYear = if (year == startYear) startMonth else 1
                val endMonthOfYear = if (year == endYear) endMonth else 12

                for (month in startMonthOfYear..endMonthOfYear) {
                    monthYearList.add(MonthYearModel(month, year))
                }
            }
        }

        return monthYearList
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