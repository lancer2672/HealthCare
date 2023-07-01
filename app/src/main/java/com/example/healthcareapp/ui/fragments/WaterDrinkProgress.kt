package com.example.healthcareapp.ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.example.healthcareapp.R
import com.example.healthcareapp.databinding.FragmentWaterDrinkProgressBinding
import com.example.healthcareapp.viewmodels.ReminderViewModel
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import java.util.Calendar


class WaterDrinkProgress : Fragment() {
    private var spinnerArray: ArrayList<String>? = null
    private lateinit var binding: FragmentWaterDrinkProgressBinding;
    private  val reminderViewModel: ReminderViewModel by activityViewModels()
    private var spinnerArrayAdapter: ArrayAdapter<String>? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_water_drink_progress, container, false)
        reminderViewModel.isLoadingDataChart.observe(viewLifecycleOwner){
            if(!it){
                if(reminderViewModel.dataChartDrunkValue.size == 0){
                    binding.chartWaterProgress.visibility = View.INVISIBLE
                }
                else{
                    binding.chartWaterProgress.visibility = View.VISIBLE
                    setUpChart(reminderViewModel.dataChartDrunkValue,reminderViewModel.dataChartMaxValue)
                }
            }
        }
        reminderViewModel.currentStreak.observe(viewLifecycleOwner){
            Log.d("STREAK CURRENT", it.toString())
            binding.streakProgress.maxProgress = it.toDouble()
            binding.streakProgress.setCurrentProgress(it.toDouble())
        }

        reminderViewModel.isLoading.observe(viewLifecycleOwner){
            if(!it){
                binding.totalDay.text = reminderViewModel.getDaysFromOldestDate().toString()
                binding.longestStreak.text = reminderViewModel.getLongestConsecutiveSequence().toString()
                reminderViewModel.getCurrentStreak()
                reminderViewModel.checkCurrentProcess();
            }
        }
        reminderViewModel.getCurrentStreak()
        reminderViewModel.getDrinkProcess();
        reminderViewModel.initMonthYearList();


        setUpSpinner();
        initTimeDataSpinner();

        return binding.root
    }
    private fun setUpSpinner(){
        spinnerArray = ArrayList()
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
                val selectedMonthYear = reminderViewModel.monthYearList[position]
                reminderViewModel.loadDataChart(selectedMonthYear.month, selectedMonthYear.year)
            }
            override fun onNothingSelected(parentView: AdapterView<*>?) {
                Log.d("OnItemSelected", "Spinner - Nothing selected")
            }
        }
    }
    private fun initTimeDataSpinner() {
        val currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        var currentIndex = 0
        for ((index, monYearModel) in reminderViewModel.monthYearList.withIndex()) {
            val item = "Tháng " + monYearModel.month + " năm " + monYearModel.year
            spinnerArray?.add(item)
            if (monYearModel.month == currentMonth && monYearModel.year == currentYear) {
                currentIndex = index
            }
        }
        spinnerArrayAdapter?.notifyDataSetChanged()
        binding.spinnerMonthYear.setSelection(currentIndex)
    }

    private fun setUpChart(drunkWaterData: ArrayList<Entry>, maxWaterData: ArrayList<Entry>) {
        val chart: LineChart = binding.chartWaterProgress
        chart.setDrawGridBackground(false)

        chart.xAxis.setDrawGridLines(false)
        chart.axisLeft.setDrawGridLines(false)
        chart.xAxis.setLabelCount(maxWaterData.size, true)

        chart.description.text = "Ngày"
        // Only show the bottom axis
        chart.xAxis.setDrawAxisLine(true)
        chart.xAxis.position = XAxis.XAxisPosition.BOTTOM

        // Format the values displayed on the X-axis
        chart.xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                val intValue = value.toInt()
                val decimalPart = value - intValue
                return if (decimalPart in 0.1..0.9) "" else intValue.toString()
            }
        }

        // Only show the left axis
        chart.axisLeft.setDrawAxisLine(true)
        chart.axisRight.isEnabled = false


        val dataSet1 = LineDataSet(maxWaterData, "Mục tiêu")
        dataSet1.color = Color.RED
        dataSet1.setDrawValues(false) // Don't draw values above data points
        dataSet1.setCircleColor(Color.RED)
        val dataSet2 = LineDataSet(drunkWaterData, "Đã uống")
        dataSet2.color = Color.GREEN
        dataSet2.setCircleColor(Color.GREEN)
        dataSet2.setDrawValues(false) // Don't draw values above data points
        // Create a LineData and add both LineDataSets to it
        val lineData = LineData(dataSet1, dataSet2)

        // Set the LineData for the LineChart
        chart.data = lineData
        chart.invalidate()
    }





}