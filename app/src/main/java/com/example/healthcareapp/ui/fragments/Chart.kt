package com.example.healthcareapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.healthcareapp.R
import com.example.healthcareapp.databinding.FragmentChartBinding
import com.example.healthcareapp.viewmodels.SurveyViewModel
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.formatter.ValueFormatter

class Chart : Fragment() {
    private var spinnerArray: ArrayList<String>? = null
    private var spinnerArrayAdapter: ArrayAdapter<String>? = null
    private val surveyViewModel :SurveyViewModel by activityViewModels()
    private lateinit var binding :FragmentChartBinding;
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chart, container, false)
        spinnerArray = ArrayList()
        surveyViewModel.getSurveyHistory()
        spinnerArrayAdapter = ArrayAdapter(
            requireActivity(),
            android.R.layout.simple_spinner_item,
            spinnerArray!!
        )
        spinnerArrayAdapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerMonthYear.adapter = spinnerArrayAdapter
        binding.spinnerMonthYear.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                Log.d("OnItemSelected", "Spinner item selected")
                val selectedMonthYear = surveyViewModel.monthYearList[position]
                surveyViewModel.getDataChartByDate(selectedMonthYear)
                val dataSet = BarDataSet(surveyViewModel.dataChart, null)
                val barData = BarData(dataSet)
                dataSet.valueFormatter = MyYAxisValueFormatter()

                setUpBarChart(barData)
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                Log.d("OnItemSelected", "Spinner - Nothing selected")
            }
        }

        surveyViewModel.isLoading.observe(viewLifecycleOwner, Observer<Boolean> { aBoolean ->
            if (!aBoolean) {
                for (monYearModel in surveyViewModel.monthYearList) {
                    val item = "Tháng " + monYearModel.month + " năm " + monYearModel.year
                    spinnerArray?.add(item)
                }
                spinnerArrayAdapter?.notifyDataSetChanged()
            }
        })
        return binding.root
    }


    private fun setUpBarChart(barData: BarData) {
        // Create a BarChart object
        val chart = binding.chart
        chart.description.isEnabled = false
        chart.setPinchZoom(false)

//        chart.isDoubleTapToZoomEnabled = false
//        chart.setScaleEnabled(false)
        chart.legend.isEnabled = false

        // Remove the layout grid
        chart.setDrawGridBackground(false)
        chart.xAxis.setDrawGridLines(false)
        chart.axisLeft.setDrawGridLines(false)


        // Remove right Y-axis
        chart.axisRight.isEnabled = false

        // Create a BarDataSet object
        val dataSet = BarDataSet(surveyViewModel.dataChart, null)
        dataSet.setDrawValues(false) // Add this line to hide the values

        // Set the colors
        val color1 = ContextCompat.getColor(requireActivity(), R.color.purple_200)
        val color2 = ContextCompat.getColor(requireActivity(), R.color.light_green)
        val color3 = ContextCompat.getColor(requireActivity(), R.color.light_yellow)
        val color4 = ContextCompat.getColor(requireActivity(), R.color.gray_400)
        dataSet.colors = listOf(color1, color2, color3, color4)

        chart.data = barData

        // Set the axes
        val xAxis = chart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawAxisLine(true) // Show X-axis line
        xAxis.valueFormatter = MonthAxisValueFormatter()
        xAxis.granularity = 1f
        xAxis.setLabelCount(dataSet.entryCount , true)

        val yAxis = chart.axisLeft
        yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
        yAxis.setDrawAxisLine(true) // Show Y-axis line
        yAxis.valueFormatter = MyYAxisValueFormatter()
        yAxis.textSize = 10f // Set Y-axis label text size
        yAxis.zeroLineWidth = 2f
        yAxis.zeroLineColor = R.color.brown
        yAxis.textColor = R.color.brown
        yAxis.labelCount = 3
        yAxis.axisMinimum = 0.0f
        yAxis.axisMaximum = 0.8f


        // Animate the chart
        chart.animateY(1000)

    }

    class MonthAxisValueFormatter : ValueFormatter() {
        override fun getAxisLabel(date: Float, axis: AxisBase): String {
            return "Ngày " + Math.round(date)
        }
    }

    class MyYAxisValueFormatter : ValueFormatter() {
        private val values =
            arrayOf("No ", "Very low ", "Low ", "Medium ", "Slightly high ", "High ", "Yes ")
        private val positions = floatArrayOf(0f, 0.15f, 0.3f, 0.5f, 0.7f, 0.85f, 1f)
        override fun getFormattedValue(value: Float): String {
            var index = -1
            var minDistance = Float.MAX_VALUE
            for (i in positions.indices) {
                val distance = Math.abs(value - positions[i])
                if (distance < minDistance) {
                    minDistance = distance
                    index = i
                }
            }
            // Make sure the index is within the bounds of our values array
            return if (index < 0 || index >= values.size) {
                ""
            } else values[index]

            // Return the value at the specified index
        }
    }
}