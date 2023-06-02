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
        binding = DataBindingUtil.setContentView(this,R.layout.activity_survey);
        viewModel = ViewModelProvider(this)[SurveyViewModel::class.java];
        binding.viewmodel = viewModel;1
        viewModel.loadQuestionList();

        viewModel.isLoading.observe(this){isLoading->
            if(!isLoading){
                Log.d("QUESTION",viewModel.questions.size.toString());
                viewModel.currentQuestionIndex.value = 0;
            }
        }

        viewModel.currentQuestionIndex.observe(this){
            if(it == viewModel.questions.size){
                viewModel.uploadQuestionnaireResult()
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent);
            }
            else{
                binding.radGrAnswers.clearCheck()
                val slideInRight = AnimationUtils.loadAnimation(this, R.anim.slide_in_right)
                val slideOutLeft = AnimationUtils.loadAnimation(this, R.anim.slide_out_left)
                binding.tvQuestion.startAnimation(slideOutLeft)
                binding.radGrAnswers.startAnimation(slideOutLeft)

                slideOutLeft.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation?) {
                        // Animation start event
                    }
                    override fun onAnimationEnd(animation: Animation?) {
                        // Animation end event
                        viewModel.loadCurrentQuestionAndAnswers()
                        for (i in 0 until binding.radGrAnswers.childCount) {
                            val radioButton = binding.radGrAnswers.getChildAt(i) as RadioButton
                            if (i < viewModel.answerList.size) {
                                radioButton.text = viewModel.answerList[i]
                                radioButton.visibility = View.VISIBLE
                            } else {
                                radioButton.visibility = View.INVISIBLE
                            }
                        }
                        binding.tvQuestion.startAnimation(slideInRight)
                        binding.radGrAnswers.startAnimation(slideInRight)
                    }

                    override fun onAnimationRepeat(animation: Animation?) {
                        // Animation repeat event
                    }
                })
            }
        }
        binding.radGrAnswers.setOnCheckedChangeListener { group, checkedId ->
            val radioButton = findViewById<RadioButton>(checkedId)
            val seletedIndex = binding.radGrAnswers.indexOfChild(radioButton);
            viewModel.selectedIndex.value = seletedIndex
        }
    }

    override fun onStart() {
        super.onStart()

    }
}