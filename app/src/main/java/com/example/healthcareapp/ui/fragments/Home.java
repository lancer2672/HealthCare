package com.example.healthcareapp.ui.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.healthcareapp.R;
import com.example.healthcareapp.databinding.FragmentHomeBinding;
import com.example.healthcareapp.ui.activities.SurveyActivity;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.List;


public class Home extends Fragment {
    private FragmentHomeBinding binding;
    public Home() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false);
        setUpBarChart();
        binding.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity(), SurveyActivity.class);
                startActivity(intent);
            }
        });
        return binding.getRoot();
    }

    private void setUpBarChart() {
        // Create a BarChart object
        BarChart chart = binding.chart;

        chart.getDescription().setEnabled(false);
        chart.setPinchZoom(false);
        chart.setDoubleTapToZoomEnabled(false);
        chart.setScaleEnabled(false);
        chart.getLegend().setEnabled(false);

        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(1, 0.15F));
        entries.add(new BarEntry(2, 0.5f));
        entries.add(new BarEntry(3, 0.4f));
        entries.add(new BarEntry(4, 0.8f));

        // Create a BarDataSet object
        BarDataSet dataSet = new BarDataSet(entries, null);
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
        String[] yValues = new String[]{"No", "Yes", "Oc"};
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
        dataSet.setColors(color2);
        // Create a BarData object
        BarData barData = new BarData(dataSet);
        // Set the data to the chart
        chart.setData(barData);
        // Animate the chart
        chart.animateY(1000);
    }
    public static class MonthAxisValueFormatter extends ValueFormatter {
        @Override
        public String getAxisLabel(float week, AxisBase axis) {
            int weekInt = Math.round(week);
            if (weekInt >= 1 && weekInt <= 4) {
                return " Week " + weekInt;
            } else {
                return "";
            }
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
