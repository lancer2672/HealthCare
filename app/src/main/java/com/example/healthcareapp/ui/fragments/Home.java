package com.example.healthcareapp.ui.fragments;

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


public class Home extends Fragment  {
    private FragmentHomeBinding binding;
    private SurveyViewModel surveyViewModel;
    private ArrayList<String> spinnerArray;
    private  ArrayAdapter<String> spinnerArrayAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false);
        surveyViewModel = new ViewModelProvider(requireActivity()).get(SurveyViewModel.class);

        surveyViewModel.isLoading().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(!aBoolean){
                    for (MonthYearModel monYearModel : surveyViewModel.getMonthYearList()) {
                        String item = "tháng " + monYearModel.getMonth() + " năm " + monYearModel.getYear();
                        spinnerArray.add(item);
                    }
                    spinnerArrayAdapter.notifyDataSetChanged();
                }
            }
        });
        CircleAngleAnimation animation = new CircleAngleAnimation(binding.circle, 240);
        animation.setDuration(1000);
        binding.circle.startAnimation(animation);

        surveyViewModel.getSurveyHistory();

        spinnerArray = new ArrayList<>();
        spinnerArrayAdapter = new ArrayAdapter<>(
                requireActivity(),
                android.R.layout.simple_spinner_item,
                spinnerArray
        );
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerMonthYear.setAdapter(spinnerArrayAdapter);
        binding.spinnerMonthYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Log.d("OnItemSelected","Spinner item selected");
                MonthYearModel selectedMonthYear = surveyViewModel.getMonthYearList().get(position);
                surveyViewModel.getDataChartByDate(selectedMonthYear);
                BarDataSet dataSet = new BarDataSet(surveyViewModel.getDataChart(), null);
                BarData barData = new BarData(dataSet);

                setUpBarChart(barData);
             }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                Log.d("OnItemSelected","Spinner - Nothing selected");
            }

        });
        return binding.getRoot();
    }

    private void setUpBarChart(BarData barData) {
        // Create a BarChart object
        BarChart chart = binding.chart;
        chart.getDescription().setEnabled(false);
        chart.setPinchZoom(false);
        chart.setDoubleTapToZoomEnabled(false);
        chart.setScaleEnabled(false);
        chart.getLegend().setEnabled(false);

        // hiển thị ngày/tháng ở trục x


        // Create a BarDataSet object
        BarDataSet dataSet = new BarDataSet(surveyViewModel.getDataChart(), null);
        // Remove the layout grid
        chart.setDrawGridBackground(false);
        chart.getXAxis().setDrawGridLines(false);
        chart.getAxisLeft().setDrawGridLines(false);


        // Set the axes
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(true); // Show X-axis line
        xAxis.setValueFormatter(new MonthAxisValueFormatter());
        xAxis.setGranularity(1f);

        YAxis yAxis = chart.getAxisLeft();
        yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        yAxis.setDrawAxisLine(true); // Show Y-axis line
        yAxis.setValueFormatter(new MyYAxisValueFormatter());
        yAxis.setTextSize(10f); // Set Y-axis label text size
        yAxis.setZeroLineWidth(2f);
        yAxis.setZeroLineColor(R.color.brown);
        yAxis.setTextColor(R.color.brown);
        yAxis.setLabelCount(3);
//        yAxis.setLabelCount(3); // Set the number of labels on the Y-axis

        ArrayList<String> labels = new ArrayList<>();
        labels.add("High");
        labels.add("Normal");
        labels.add("Low");
//        yAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        // Remove right Y-axis
        chart.getAxisRight().setEnabled(false);
        // Add some data
        // Set the colors
        int color1 = ContextCompat.getColor(requireActivity(), R.color.purple_200);
        int color2 = ContextCompat.getColor(requireActivity(), R.color.light_green);
        int color3 = ContextCompat.getColor(requireActivity(), R.color.light_yellow);
        int color4 = ContextCompat.getColor(requireActivity(), R.color.gray_400);
        dataSet.setColors(color1, color2, color3, color4);

        chart.setData(barData);

        // Create a BarData object
//        BarData barData = new BarData(dataSet);
//        // Set the data to the chart
//        chart.setData(barData);
        // Animate the chart
        chart.animateY(1000);
    }
    public static class MonthAxisValueFormatter extends ValueFormatter {
        @Override
        public String getAxisLabel(float date, AxisBase axis) {
                return "Ngày " + Math.round(date);
        }
    }
    public static class MyYAxisValueFormatter extends ValueFormatter {
        private final String[] values = new String[]{"No ", "Very low ", "Low ", "Medium ", "Slightly high ", "High ", "Yes "};
        private final float[] positions = new float[]{0f, 0.15f, 0.3f, 0.5f, 0.7f, 0.85f, 1f};
        @Override
        public String getFormattedValue(float value) {
            int index = -1;
            float minDistance = Float.MAX_VALUE;
            for (int i = 0; i < positions.length; i++) {
                float distance = Math.abs(value - positions[i]);
                if (distance < minDistance) {
                    minDistance = distance;
                    index = i;
                }
            }
            // Make sure the index is within the bounds of our values array
            if (index < 0 || index >= values.length) {
                return "";
            }

            // Return the value at the specified index
            return values[index];
        }
    }


}
