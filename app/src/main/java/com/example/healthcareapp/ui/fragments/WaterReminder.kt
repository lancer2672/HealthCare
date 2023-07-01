package com.example.healthcareapp.ui.fragments

import android.app.DatePickerDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.healthcareapp.R
import com.example.healthcareapp.databinding.FragmentWaterReminderBinding
import com.example.healthcareapp.ui.adapters.ListRecordAdapter
import com.example.healthcareapp.viewmodels.ReminderViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class WaterReminder : Fragment() {

    private lateinit var binding: FragmentWaterReminderBinding;
    private  val reminderViewModel: ReminderViewModel by activityViewModels()
    private lateinit var adapterHistory: ListRecordAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_water_reminder, container, false)
        setObservation();
        setBtnOnClick();
        adapterHistory = ListRecordAdapter(requireActivity(),reminderViewModel,reminderViewModel.drinkHistoryList)
        reminderViewModel.historyAdapter = adapterHistory
        reminderViewModel.getTodayHistory()
        return binding.root
    }
    private fun setObservation(){
        reminderViewModel.currentProgress.observe(viewLifecycleOwner){
            if(it.compareTo(reminderViewModel.maxProgress.value!!) == 1){
                binding.waterCircularProgress.setCurrentProgress(reminderViewModel.maxProgress.value!!);
            }
            else{
                binding.waterCircularProgress.setCurrentProgress(it);
            }
        }
        reminderViewModel.maxProgress.observe(viewLifecycleOwner){
            binding.waterCircularProgress.maxProgress = it
        }
    }
    private fun setBtnOnClick(){
        binding.btn100ml.setOnClickListener {
            updateProgressCircle(100.0);
        }
        binding.btn200ml.setOnClickListener {
            updateProgressCircle(200.0);
        }
        binding.btn300ml.setOnClickListener {
            updateProgressCircle(300.0);
        }
        binding.btn400ml.setOnClickListener {
            updateProgressCircle(400.0);
        }
        binding.settingBtn.setOnClickListener {
            parentFragmentManager.beginTransaction().addToBackStack("ab").add(R.id.main_fragment, ReminderSetting()).commit()
        }
        binding.progress.setOnClickListener {
            parentFragmentManager.beginTransaction().addToBackStack("ab").add(R.id.main_fragment, WaterDrinkProgress()).commit()
        }
        binding.historyBtn.setOnClickListener {
            showHistory();
        }
    }
    private fun updateProgressCircle(value: Double){
        if(reminderViewModel.currentProgress.value!! >= reminderViewModel.maxProgress.value!!) return;
        val newProgress = binding.waterCircularProgress.progress.plus(value)
        if (newProgress >= reminderViewModel.maxProgress.value!!) {
            handleExceededMaxProgress()
            Log.d("STREAK OLD ", reminderViewModel.currentStreak.value.toString())
            reminderViewModel.currentStreak.value = reminderViewModel.currentStreak.value?.plus(1);
            Log.d("STREAK NEW ", reminderViewModel.currentStreak.value.toString())
            reminderViewModel.currentProgress.value = reminderViewModel.maxProgress.value
        } else {
            reminderViewModel.currentProgress.value = newProgress
        }
        reminderViewModel.updateDrinkHistory(value);
    }
    private fun handleExceededMaxProgress() {
        showCongratulationsDialog();
    }
    private fun showCongratulationsDialog() {
        val bottomSheetDialog = Dialog(requireActivity())
        bottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        bottomSheetDialog.setContentView(R.layout.congratulations_reminder_dialog)
        bottomSheetDialog.show()
        bottomSheetDialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        //        circle.triggerAnimation();
        bottomSheetDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        bottomSheetDialog.window!!.attributes.windowAnimations = R.style.BottomSheetAnimation
        bottomSheetDialog.window!!.setGravity(Gravity.CENTER)
    }
    private fun showHistory(){
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val bottomSheetDialog = Dialog(requireActivity())
        bottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        bottomSheetDialog.setContentView(R.layout.drink_history_dialog)
        reminderViewModel.getDrinkHistory(Date());
        val rec = bottomSheetDialog.findViewById<RecyclerView>(R.id.rec_drink_records)
        val timeContainer = bottomSheetDialog.findViewById<LinearLayout>(R.id.time_history)
        val time = bottomSheetDialog.findViewById<TextView>(R.id.tv_time_history)
        time.text = dateFormat.format(Date())
        timeContainer.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(requireActivity(), { _, year, month, dayOfMonth ->
                val date = Calendar.getInstance()
                date.set(year, month, dayOfMonth)
                time.text = dateFormat.format(date.time)
                reminderViewModel.selectedDate = date.time;
                reminderViewModel.getDrinkHistory(date.time);
            }, year, month, day)
            datePickerDialog.show()
        }
        rec.adapter = reminderViewModel.historyAdapter
        bottomSheetDialog.show()
        bottomSheetDialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        //        circle.triggerAnimation();
        bottomSheetDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        bottomSheetDialog.window!!.attributes.windowAnimations = R.style.BottomSheetAnimation
        bottomSheetDialog.window!!.setGravity(Gravity.CENTER)
    }
}