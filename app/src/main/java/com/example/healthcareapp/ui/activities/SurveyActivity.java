package com.example.healthcareapp.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.healthcareapp.R;
import com.example.healthcareapp.databinding.ActivitySurveyBinding;
import com.example.healthcareapp.viewmodels.SurveyViewModel;

import java.util.List;

public class SurveyActivity extends AppCompatActivity {
    private ActivitySurveyBinding binding;
    private SurveyViewModel viewModel;
    private List<String> questions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_survey);
        viewModel = new ViewModelProvider(this).get(SurveyViewModel.class);
        binding.setViewmodel(viewModel);
        questions = viewModel.getQuestionList();

        binding.tvQuestion.setText(viewModel.getQuestionList().get(0));
        viewModel.setCurrentQuestionIndex(viewModel.getCurrentQuestionIndex() + 1);
        binding.tvQuestion.setText(questions.get(viewModel.getCurrentQuestionIndex()));
        binding.btnNextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation slideOutLeft = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_left);
                slideOutLeft.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        int currentIndex = viewModel.getCurrentQuestionIndex();
                        viewModel.setCurrentQuestionIndex(currentIndex + 1);
                        currentIndex++;
                        if(currentIndex < questions.size() - 1)
                            binding.tvQuestion.setText(questions.get(currentIndex));
                        else {
                            binding.btnNextQuestion.setText("Done");
                        }
                        Animation slideInRight = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_right);
                        binding.questionAnswerLayout.startAnimation(slideInRight);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                binding.questionAnswerLayout.startAnimation(slideOutLeft);
            }
        });

    }
}