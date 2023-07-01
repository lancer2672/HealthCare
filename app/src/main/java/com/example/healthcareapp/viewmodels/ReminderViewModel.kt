package com.example.healthcareapp.viewmodels

import android.R.attr.entries
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.healthcareapp.data.models.DrinkHistoryModel
import com.example.healthcareapp.data.models.DrinkProcess
import com.example.healthcareapp.data.models.MonthYearModel
import com.example.healthcareapp.data.repositories.DrinkProcessRepository
import com.example.healthcareapp.data.repositories.UserRepository
import com.example.healthcareapp.ui.adapters.ListRecordAdapter
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.utils.EntryXComparator
import com.google.firebase.Timestamp
import java.lang.Integer.max
import java.util.Calendar
import java.util.Collections
import java.util.Date
import java.util.concurrent.TimeUnit


class ReminderViewModel : ViewModel() {
    val intervalSelections = arrayListOf<Int>(1,2,3,4)
    var bedHour:Int = 23
    var wakeupHour:Int = 7
    var bedMin:Int = 0
    var wakeupMin:Int = 0
    var intervalTime: Int = 4
    var maxProgress = MutableLiveData<Double>(2000.0);
    var currentProgress  =  MutableLiveData<Double>(0.0);
    var isLoadingDataChart  =  MutableLiveData(false);
    var isLoading  =  MutableLiveData(false);
    var historyAdapter: ListRecordAdapter? = null;
    var selectedDate:Date? = null;
    val drinkHistoryList = arrayListOf<DrinkHistoryModel>()
    val drinkProcessList = arrayListOf<DrinkProcess>()
    var dataChartMaxValue: ArrayList<Entry> = arrayListOf()
    var dataChartDrunkValue: ArrayList<Entry> = arrayListOf()
    val monthYearList: MutableList<MonthYearModel> = mutableListOf()
    var currentStreak = MutableLiveData(0);
    fun initMonthYearList(){
        monthYearList.clear()
        for (month in 4..8) {
            monthYearList.add(MonthYearModel(month, 2023))
        }
    }
    fun getTodayHistory(){
        AuthViewModel.getUser()
            ?.let { UserRepository.instance?.getDrinkHistory( it.userId,Timestamp.now().toDate()){
                val totalValue = it.sumOf { it.value }
                currentProgress.value = totalValue
            } }
    }
    fun getDrinkProcess(){
        isLoading.value = true;
        AuthViewModel.getUser()
            ?.let { DrinkProcessRepository.instance?.getDrinkProcess( it.userId){
                drinkProcessList.clear()
                drinkProcessList.addAll(it);
                isLoading.value = false;
            } }
    }
//    fun loadDataChart(month: Int, year: Int) {
//        val filteredList = drinkProcessList.filter {
//            val createdAt = it.date.toDate()
//            val m =  createdAt.month + 1
//            val y =  createdAt.year + 1900
//            m== month && y == year
//        }
//        Log.d("CreatedAt filteredList size", filteredList.size.toString());
//
//        val entries1 = ArrayList<Entry>()
//        val entries2 = ArrayList<Entry>()
//        entries2.add(Entry(0f, 800f))
//        entries2.add(Entry(1f, 100f))
//        entries2.add(Entry(2f, 300f))
//        entries2.add(Entry(3f, 800f))
//        entries1.add(Entry(0f, 100f))
//        entries1.add(Entry(1f, 200f))
//        entries1.add(Entry(2f, 600f))
//        entries1.add(Entry(3f, 2200f))
//        filteredList.forEach { m ->
//            val timestamp = m.date
//            val date = timestamp.toDate()
//            val calendar = Calendar.getInstance()
//            calendar.time = date
//            val day = calendar.get(Calendar.DAY_OF_MONTH)
//            Log.d("Value",m.value.toString().toFloat().toString());
//            Log.d("Value Day ",day.toFloat().toString());
//            entries1.add(Entry(day.toFloat(), m.value.toFloat()))
//            entries2.add(Entry(day.toFloat(), m.maxValue.toFloat()))
//        }
//        dataChartMaxValue.clear()
//        dataChartDrunkValue.clear()
//
//        dataChartDrunkValue.addAll(entries1)
//        dataChartMaxValue.addAll(entries2)
//    }
fun getLongestConsecutiveSequence(): Int {
    val sortedDates = drinkProcessList.filter { it.value == it.maxValue }.map { it.date.toDate() }.sorted()
    var maxLength = 0
    var currentLength = 1
    for (i in 1 until sortedDates.size) {
        val currentDate = sortedDates[i]
        val previousDate = sortedDates[i - 1]
        val differenceInDays = TimeUnit.DAYS.convert(currentDate.time - previousDate.time, TimeUnit.MILLISECONDS)
        if (differenceInDays <= 1L) {
            currentLength++
        } else {
            maxLength = max(maxLength, currentLength)
            currentLength = 1
        }
    }
    return max(maxLength, currentLength)
}

    fun getCurrentStreak(): Int {
        val sortedDates = drinkProcessList.filter { it.value == it.maxValue }.map { it.date.toDate().time }.sortedDescending()
        val currentDate = System.currentTimeMillis()

        var streak = 0
        var previousDate: Long? = null // Thêm biến để theo dõi ngày trước đó
        for (i in sortedDates.indices) {
            val currentDateMillis = sortedDates[i]
            if (previousDate == null || currentDate - currentDateMillis <= TimeUnit.DAYS.toMillis(streak.toLong())) {
                streak++
            } else {
                break
            }
            previousDate = currentDateMillis // Lưu ngày hiện tại để so sánh với ngày tiếp theo
        }
        Log.d("Streak",streak.toString())
        currentStreak.value = streak;
        return streak
    }

    fun checkCurrentProcess(){
        AuthViewModel.getUser()?.let {
            DrinkProcessRepository.instance?.checkCurrentDrinkProcess(it.userId){
                if(it){
                    Log.d("Drink Process Check",it.toString());
                    Log.d("Drink Process ",currentStreak.value.toString());

                    currentStreak.value = currentStreak.value!!.plus(1);
                    Log.d("Drink Process Check",currentStreak.value.toString());
                }
            }
        }
    }
    fun getDaysFromOldestDate(): Long {
        Log.d("drinkProcessList getDaysFromOldestDate" , drinkProcessList.size.toString())
            val oldestDate = drinkProcessList.minByOrNull { it.date }?.date
            val currentDate = Timestamp.now()
            return if (oldestDate != null) {
                TimeUnit.MILLISECONDS.toDays(currentDate.toDate().time - oldestDate.toDate().time)
            } else {
                0
            }
        }
    fun loadDataChart(month:Int, year:Int){
        isLoadingDataChart.value = true;
        AuthViewModel.getUser()
            ?.let {
                DrinkProcessRepository.instance?.getDrinkProcessByMonth(it.userId, month -1, year-1900) {
                    dataChartMaxValue.clear();
                    dataChartDrunkValue.clear();
                    Log.d("List size",it.size.toString())
                    it.forEach { m ->
                        val timestamp = m.date
                        val date = timestamp.toDate()
                        val calendar = Calendar.getInstance()
                        calendar.time = date
                        val day = calendar.get(Calendar.DAY_OF_MONTH)
                        dataChartDrunkValue.add(Entry(day.toFloat(), m.value.toFloat()))
                        dataChartMaxValue.add(Entry(day.toFloat(), m.maxValue.toFloat()))
                    }
                    Collections.sort(dataChartDrunkValue, EntryXComparator())
                    Collections.sort(dataChartMaxValue, EntryXComparator())
                    isLoadingDataChart.value = false;
                }
            }

    }
    fun getDrinkHistory(date:Date){
        AuthViewModel.getUser()
            ?.let { UserRepository.instance?.getDrinkHistory( it.userId,date){
                drinkHistoryList.clear()
                drinkHistoryList.addAll(it)
                historyAdapter?.notifyDataSetChanged()
            } }
    }
    fun updateDrinkHistory(value: Double){
        val drinkHistoryModel = DrinkHistoryModel(Timestamp.now(),value, maxProgress.value!!)
        AuthViewModel.getUser()
            ?.let { UserRepository.instance?.updateDrinkHistory(drinkHistoryModel, it.userId){
                Log.d("Max Process", currentProgress.value.toString());
                DrinkProcessRepository.instance?.create(it.userId, DrinkProcess(Timestamp.now(),currentProgress.value!!, maxProgress.value!!,it.userId))
            }
            }
    }
    fun deleteDrinkHistory(){
        AuthViewModel.getUser()
            ?.let { UserRepository.instance?.deleteDrinkHistory(it.userId,Timestamp.now()) }
    }
    fun deleteItemDrinkHistory(createdAt: Timestamp){
        AuthViewModel.getUser()
            ?.let { UserRepository.instance?.deleteItemDrinkHistory(it.userId,createdAt){
                selectedDate?.let { it1 -> getDrinkHistory(it1) }
                historyAdapter?.notifyDataSetChanged()
            } }
    }
}