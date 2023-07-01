package com.example.healthcareapp.data.repositories

import android.util.Log
import com.example.healthcareapp.data.models.ChatModel
import com.example.healthcareapp.data.models.DrinkProcess
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

class DrinkProcessRepository {
    private val  db : FirebaseFirestore = FirebaseFirestore.getInstance()
    private val  drinkProcessRef = FirebaseFirestore.getInstance().collection("drink_process")
    companion object {
        var instance: DrinkProcessRepository? = null
            get() {
                if (field == null) {
                    field = DrinkProcessRepository()
                }
                return field
            }
            private set
    }
    fun create(userId: String, drinkProcess: DrinkProcess, onSuccess: (() -> Unit)? = null, onFailure: (() -> Unit)? = null) {
        val date = drinkProcess.date.toDate()
        drinkProcessRef.whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { documents ->
                val existingDocument = documents.firstOrNull {
                    val drinkProcess = it.toObject(DrinkProcess::class.java)
                    val createdAt = drinkProcess.date.toDate()
                    createdAt.getDate() == date.getDate() &&
                            createdAt.getMonth() == date.getMonth() &&
                            createdAt.year == date.year
                }
                if (existingDocument == null) {
                    // Không tồn tại DrinkProcess khác trong cùng ngày, tạo mới
                    drinkProcessRef.add(drinkProcess)
                        .addOnSuccessListener {
                            if (onSuccess != null) {
                                onSuccess()
                            }
                            Log.w("Drink process", "Create successfully")
                        }
                        .addOnFailureListener { e ->
                            if (onFailure != null) {
                                onFailure()
                            }
                            Log.w("Drink process", "Error adding document", e)
                        }
                } else {
                    // Đã tồn tại DrinkProcess khác trong cùng ngày, cập nhật giá trị
                    val existingDrinkProcess = existingDocument.toObject(DrinkProcess::class.java)
                    existingDrinkProcess.value = drinkProcess.value
                    existingDocument.reference.set(existingDrinkProcess)
                        .addOnSuccessListener {
                            if (onSuccess != null) {
                                onSuccess()
                            }
                            Log.w("Drink process", "Update successfully")
                        }
                        .addOnFailureListener { e ->
                            if (onFailure != null) {
                                onFailure()
                            }
                            Log.w("Drink process", "Error updating document", e)
                        }
                }
            }
    }
    fun getDrinkProcessByMonth(userId: String, month: Int, year: Int, onSuccess: (ArrayList<DrinkProcess>) -> Unit) {
        drinkProcessRef.whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { documents ->
                val drinkProcesses = documents.map { it.toObject(DrinkProcess::class.java) }
                    .filter {
                        val createdAt = it.date.toDate()
                        createdAt.getMonth() == month && createdAt.year == year
                    }
                Log.d("Drink Process",drinkProcesses.size.toString())
                onSuccess(ArrayList(drinkProcesses))
            }
    }
    fun getDrinkProcess(userId: String, onSuccess: (ArrayList<DrinkProcess>) -> Unit) {
        drinkProcessRef.whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { documents ->
                val drinkProcesses = documents.map { it.toObject(DrinkProcess::class.java) }
                onSuccess(ArrayList(drinkProcesses))
            }
    }

    fun checkCurrentDrinkProcess(userId: String, onSuccess: (Boolean) -> Unit) {
        drinkProcessRef.whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { documents ->
                val today = Calendar.getInstance()
                val result = documents.map { it.toObject(DrinkProcess::class.java) }
                    .any {
                        val createdAt = Calendar.getInstance()
                        createdAt.time = it.date.toDate()
                        createdAt.get(Calendar.DATE) == today.get(Calendar.DATE) &&
                                createdAt.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
                                createdAt.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                                it.value == it.maxValue
                    }
                Log.d("Result Check ",result.toString());
                onSuccess(result)
            }
    }

}