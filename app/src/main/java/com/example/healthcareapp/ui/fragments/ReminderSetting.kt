package com.example.healthcareapp.ui.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.healthcareapp.R
import com.example.healthcareapp.databinding.FragmentReminderSettingBinding
import com.example.healthcareapp.utils.Formater
import com.example.healthcareapp.viewmodels.ReminderViewModel
import java.util.Calendar

class ReminderSetting : Fragment() {

    private lateinit var binding: FragmentReminderSettingBinding;
    private  val reminderViewModel: ReminderViewModel  by activityViewModels()
    private lateinit var sharedPreferences: SharedPreferences
    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setOnTouchListener { view, _ -> return@setOnTouchListener true }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPreferences = requireActivity().getSharedPreferences("time_preferences", Context.MODE_PRIVATE)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_reminder_setting, container, false)
        binding.tvBedTime.text = Formater.formatTime(reminderViewModel.bedHour,reminderViewModel.bedMin);
        binding.tvWakeUpTime.text = Formater.formatTime(reminderViewModel.wakeupHour,reminderViewModel.wakeupMin);

        val intervalItems = reminderViewModel.intervalSelections .map { "$it hour${if (it > 1) "s" else ""}" }
        val adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_item,intervalItems )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spnIntervalTime.adapter = adapter
        setOnClick()
        reminderViewModel.maxProgress.observe(viewLifecycleOwner){
            binding.tvMaxProgress.text = it.toString()        }
        return binding.root
    }
    private fun setOnClick(){
        binding.tvMaxProgress.setOnClickListener {
            show();
        }
        binding.spnIntervalTime.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Xử lý sự kiện khi người dùng chọn một mục trong Spinner
                val selectedItem = parent?.getItemAtPosition(position)
                reminderViewModel.intervalTime = reminderViewModel.intervalSelections[position]
                Log.d("Interval new value" ,
                    reminderViewModel.intervalSelections[position].toString()
                )
                val editor = sharedPreferences.edit()
                editor.putInt("interval", reminderViewModel.intervalSelections[position])
                editor.apply()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Xử lý sự kiện khi không có mục nào được chọn
            }
        }

        binding.tvBedTime.setOnClickListener {
            showTimePicker{hour,min ->
                val editor = sharedPreferences.edit()
                binding.tvBedTime.text = Formater.formatTime(hour,min);
                editor.putInt("bed_time_hour", hour)
                editor.putInt("bed_time_minute", min)
                editor.apply()
                reminderViewModel.bedHour = hour
                reminderViewModel.bedMin = min
            }
        }
        binding.tvWakeUpTime.setOnClickListener {
            showTimePicker{hour,min ->
                val editor = sharedPreferences.edit()
                binding.tvWakeUpTime.text = Formater.formatTime(hour,min);
                editor.putInt("wake_up_hour", hour)
                editor.putInt("wake_up_minute", min)
                editor.apply()
                reminderViewModel.wakeupHour = hour
                reminderViewModel.wakeupMin = min
            }
        }
    }
    private fun showTimePicker(callback: (Int, Int)->Unit){
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(requireContext(), { _, selectedHour, selectedMinute ->
            callback(selectedHour,selectedMinute)
        }, hour, minute, true)
        timePickerDialog.show()
    }
    fun show() {
        val bottomSheetDialog = Dialog(requireActivity())
        bottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        bottomSheetDialog.setContentView(R.layout.set_max_value_dialog)
        val edtTime = bottomSheetDialog.findViewById<EditText>(R.id.edt_new_value)
        val saveBtn = bottomSheetDialog.findViewById<ImageView>(R.id.btn_save_new_value)
        saveBtn.setOnClickListener {
            reminderViewModel.maxProgress.value = edtTime.text.toString().toDoubleOrNull()?: 2000.0
            //observe change o fragmnet waterreminder
            reminderViewModel.currentProgress.value = 0.0
            reminderViewModel.deleteDrinkHistory();
            val editor = sharedPreferences.edit()
            editor.putString("max_progress", reminderViewModel.maxProgress.value.toString())
            editor.apply()
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.show()
        bottomSheetDialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        bottomSheetDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        bottomSheetDialog.window!!.attributes.windowAnimations = R.style.BottomSheetAnimation
        bottomSheetDialog.window!!.setGravity(Gravity.BOTTOM)
    }
}