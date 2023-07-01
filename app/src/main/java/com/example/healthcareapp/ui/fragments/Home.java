package com.example.healthcareapp.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.healthcareapp.R;
import com.example.healthcareapp.data.models.MeditationModel;
import com.example.healthcareapp.databinding.FragmentHomeBinding;
import com.example.healthcareapp.ui.activities.Meditation;
import com.example.healthcareapp.ui.activities.SurveyActivity;
import com.example.healthcareapp.viewmodels.AuthViewModel;
import com.example.healthcareapp.viewmodels.MeditationViewModel;
import com.example.healthcareapp.viewmodels.SurveyViewModel;

import java.util.Objects;


public class Home extends Fragment  {
    private FragmentHomeBinding binding;
    private SurveyViewModel surveyViewModel;
    private MeditationViewModel meditationViewModel;
    private static final int SURVEY_REQUEST_CODE = 1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false);
        surveyViewModel = new ViewModelProvider(requireActivity()).get(SurveyViewModel.class);
        meditationViewModel = new ViewModelProvider(requireActivity()).get(MeditationViewModel.class);
        if(AuthViewModel.Companion.getUser() != null)
        {
            binding.tvWelcome.setText("Good morning " + Objects.requireNonNull(AuthViewModel.Companion.getUser()).getDisplayName());
        }

        setOnClickBtn();
        return binding.getRoot();
    }
    public void setOnClickBtn(){
        binding.btnStartSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity(), SurveyActivity.class);
                startActivityForResult(intent, SURVEY_REQUEST_CODE);
            }
        });
        binding.btnStartMeditation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MeditationModel startItem = meditationViewModel.getStartSession();
                if(startItem != null){
                    Objects.requireNonNull(meditationViewModel.getMeditationListenr()).startSession(startItem);
                }
            }
        });
        binding.btnStartMeditationSleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction().addToBackStack("as").add(R.id.main_fragment, new MeditationList()).commit();
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SURVEY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Lấy kết quả trả về từ data
            Float result = data.getFloatExtra("result",0f);
            // Cập nhật giá trị của LiveData
            surveyViewModel.getPredictedResult().setValue(Float.valueOf(result));
        }
    }

}
