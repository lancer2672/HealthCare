package com.example.healthcareapp.ui.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.healthcareapp.R;
import com.example.healthcareapp.databinding.FragmentHomeBinding;
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

    // Declare the Y-axis labels.


    public Home() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false);
        setUpBarChart();
        return binding.getRoot();
    }

    private void setUpBarChart() {
        // Create a BarChart object
        BarChart chart = binding.chart;

        // Remove the layout grid
        chart.setDrawGridBackground(false);
        chart.getXAxis().setDrawGridLines(false);
        chart.getAxisLeft().setDrawGridLines(false);

        // Set the axes
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(true); // Show X-axis line
        xAxis.setValueFormatter(new MonthAxisValueFormatter());

        YAxis yAxis = chart.getAxisLeft();
        yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        yAxis.setDrawAxisLine(true); // Show Y-axis line
        yAxis.setTextSize(10f); // Set Y-axis label text size
        yAxis.setLabelCount(3); // Set the number of labels on the Y-axis

        ArrayList<String> labels = new ArrayList<>();
        labels.add("High");
        labels.add("Normal");
        labels.add("Low");
//        yAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        // Remove right Y-axis
        chart.getAxisRight().setEnabled(false);
        // Add some data
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(1, 40f)); // High
        entries.add(new BarEntry(2, 25f)); // Normal
        entries.add(new BarEntry(3, 10f)); // Low
        entries.add(new BarEntry(4, 40f)); // High
        entries.add(new BarEntry(5, 25f)); // Normal
        entries.add(new BarEntry(6, 10f)); // Low
        entries.add(new BarEntry(7, 40f)); // High
        entries.add(new BarEntry(8, 25f)); // Normal
        entries.add(new BarEntry(9, 10f)); // Low
        // Create a BarDataSet object
        BarDataSet dataSet = new BarDataSet(entries, "My Data");
        // Set the colors
        dataSet.setColors(new int[]{Color.RED, Color.GREEN, Color.BLUE});
        // Create a BarData object
        BarData barData = new BarData(dataSet);
        // Set the data to the chart
        chart.setData(barData);
        // Animate the chart
        chart.animateY(1000);
    }
    public static class MonthAxisValueFormatter extends ValueFormatter {
        private final String[] mMonths = new String[]{
                "Jan", "Feb", "Mar", "Apr", "May", "Jun",
                "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
        };
        @Override
        public String getAxisLabel(float value, AxisBase axis) {
            int month = (int) value - 1; // Adjust the value to match array index
            if (month >= 0 && month < mMonths.length) {
                return mMonths[month];
            } else {
                return "";
            }
        }

    }
}
