package com.example.healthcareapp.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.healthcareapp.R
import com.example.healthcareapp.databinding.ActivitySurveyBinding
import com.example.healthcareapp.viewmodels.SurveyViewModel

class SurveyActivity : AppCompatActivity() {
    private lateinit var binding:ActivitySurveyBinding;
    private lateinit var viewModel:SurveyViewModel;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[SurveyViewModel::class.java];
        binding = DataBindingUtil.setContentView(this,R.layout.activity_survey);
        binding.viewmodel = viewModel;
        setObservation();
        viewModel.loadQuestionList();
        setListener();
    }

    private fun setObservation(){
        viewModel.isLoading.observe(this){isLoading->
            if(isLoading){
                binding.surveyBtnContainer.visibility = View.GONE;
                binding.surveyProgress.visibility = View.VISIBLE
                binding.tvConfidential.visibility = View.GONE
            }
            else{
                Log.d("QUESTION",viewModel.questions.size.toString());
                viewModel.currentQuestionIndex.value = 0;
                binding.surveyBtnContainer.visibility = View.VISIBLE;
                binding.surveyProgress.visibility = View.GONE
                binding.tvConfidential.visibility = View.VISIBLE
            }
        }

        viewModel.isSelectAnswer.observe(this){isSelectAnswer->
            if(isSelectAnswer){
                binding.radGrAnswers.visibility = View.VISIBLE
                binding.edtAnswer.visibility = View.INVISIBLE
            }
            else{
                binding.radGrAnswers.visibility = View.INVISIBLE
                binding.edtAnswer.visibility = View.VISIBLE
            }
        }
        viewModel.currentQuestionIndex.observe(this){
            if(it == viewModel.questions.size){
                viewModel.uploadQuestionnaireResult()
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent);
            }
            else{
                val slideInRight = AnimationUtils.loadAnimation(this, R.anim.slide_in_right)
                //wait for radGrAnswers or editAnswer visibility change

                val slideOutLeft = AnimationUtils.loadAnimation(this, R.anim.slide_out_left )
                //Not start animation to render the first item
                if(it == 0){
                    viewModel.loadCurrentQuestionAndAnswers()
                    if(viewModel.isSelectAnswer.value == true){
                        binding.radGrAnswers.startAnimation(slideInRight)
                    }
                    else{
                        binding.edtAnswer.startAnimation(slideInRight)
                    }
                    binding.tvQuestion.startAnimation(slideInRight)
                    loadRadioBtns();
                }
                else{
                    if(viewModel.isSelectAnswer.value == true){
                        binding.radGrAnswers.startAnimation(slideOutLeft)
                    }
                    else{
                        binding.edtAnswer.startAnimation(slideOutLeft)
                    }
                    binding.tvQuestion.startAnimation(slideOutLeft)
                    binding.radGrAnswers.clearCheck()
                    slideOutLeft.setAnimationListener(object : Animation.AnimationListener {
                        override fun onAnimationStart(animation: Animation?) {
                        }
                        override fun onAnimationEnd(animation: Animation?) {
                            viewModel.loadCurrentQuestionAndAnswers()
                            loadRadioBtns();
                            // Animation end event
                            if(viewModel.isSelectAnswer.value == true){
                                binding.radGrAnswers.startAnimation(slideInRight)
                            }
                            else{
                                binding.edtAnswer.startAnimation(slideInRight)
                            }
                            binding.tvQuestion.startAnimation(slideInRight)
                        }
                        override fun onAnimationRepeat(animation: Animation?) {
                        }
                    })
                }
            }
        }
    }
    private fun setListener(){
        binding.radGrAnswers.setOnCheckedChangeListener { group, checkedId ->
            val radioButton = findViewById<RadioButton>(checkedId)
            val selectedIndex = binding.radGrAnswers.indexOfChild(radioButton);
            viewModel.selectedIndex.value = selectedIndex
        }
    }
    //set text for each radio btn
    private fun loadRadioBtns(){
        for (i in 0 until binding.radGrAnswers.childCount) {
            val radioButton = binding.radGrAnswers.getChildAt(i) as RadioButton
            if (i < viewModel.answerList.size) {
                radioButton.text = viewModel.answerList[i]
                radioButton.visibility = View.VISIBLE
            } else {
                radioButton.visibility = View.INVISIBLE
            }
        }
    }

}