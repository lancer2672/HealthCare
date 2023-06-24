package com.example.healthcareapp.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.healthcareapp.R;
import com.example.healthcareapp.data.models.MonthYearModel;
import com.example.healthcareapp.databinding.FragmentHomeBinding;
import com.example.healthcareapp.ui.activities.SurveyActivity;
import com.example.healthcareapp.ui.custom_components.CircleAngleAnimation;
import com.example.healthcareapp.viewmodels.SignUpViewModel;
import com.example.healthcareapp.viewmodels.SurveyViewModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Home extends Fragment  {
    private FragmentHomeBinding binding;
    private SurveyViewModel surveyViewModel;
    private static final int SURVEY_REQUEST_CODE = 1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false);
        surveyViewModel = new ViewModelProvider(requireActivity()).get(SurveyViewModel.class);
        binding.btnStartSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity(), SurveyActivity.class);
                startActivityForResult(intent, SURVEY_REQUEST_CODE);
            }
        });

        return binding.getRoot();
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
