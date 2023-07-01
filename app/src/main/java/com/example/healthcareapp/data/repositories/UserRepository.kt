package com.example.healthcareapp.data.repositories

import android.util.Log
import com.example.healthcareapp.data.models.DrinkHistoryModel
import com.example.healthcareapp.data.models.MeditationHistory
import com.example.healthcareapp.data.models.User
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.ArrayList
import java.util.Date


class UserRepository private constructor(){
    private val  db : FirebaseFirestore = FirebaseFirestore.getInstance()
    companion object {
        var instance: UserRepository? = null
            get() {
                if (field == null) {
                    field = UserRepository()
                }
                return field
            }
            private set
    }
    fun createUser ( user: User)
    {
        db.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d("USER", "DocumentSnapshot written with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("USER", "Error adding document", e)
            }
    }
    suspend fun getUserFromDb(userId: String, onSuccess: (User) -> Unit,onFailure : (() -> Unit)? = null) {
        val query = db.collection("users").whereEqualTo("userId", userId)
        try {
            val snapshot = query.get().await()
            if (snapshot.documents.isNotEmpty()) {
                val user = snapshot.documents.first().toObject(User::class.java)
                if (user != null) {
                    Log.w("USER", " user with drinkHistorySize: ${user.drinkHistory.size}")

                    onSuccess(user)
                } else {
                    Log.w("USER", "Error getting user with userId: $userId")
                }
            } else {
                Log.w("USER", "No user found with userId: $userId")
            }
        } catch (e: Exception) {
            if (onFailure != null) {
                onFailure()
            };
            Log.w("USER", "Error getting user with userId: $userId", e)
        }
    }
    fun updateMeditationHistory(meditationHistory: MeditationHistory, userId: String) {
        val userRef = db.collection("users").whereEqualTo("userId", userId)
            userRef.get().addOnSuccessListener { documents ->
                for (document in documents) {
                    val user = document.toObject(User::class.java)
                    user.meditationHistory.add(meditationHistory)
                    db.collection("users").document(document.id).set(user)
                }
                Log.d("Meditation","Add to history successfully")
            }
    }
    fun updateDrinkHistory(drinkHistoryModel: DrinkHistoryModel, userId: String, onSuccess: (User) -> Unit) {
        val userRef = db.collection("users").whereEqualTo("userId", userId)
        userRef.get().addOnSuccessListener { documents ->
            for (document in documents) {
                val user = document.toObject(User::class.java)
                Log.d("Drink", user.drinkHistory.size.toString())
                user.drinkHistory.add(drinkHistoryModel)
                db.collection("users").document(document.id).set(user)
                Log.d("Drink", user.drinkHistory.size.toString())
                onSuccess(user)
            }
            Log.d("Drink", "Add to history successfully")
        }
    }

    fun getDrinkHistory(userId: String, date: Date, onSuccess: (ArrayList<DrinkHistoryModel>) -> Unit) {
        val userRef = db.collection("users").whereEqualTo("userId", userId)
        userRef.get().addOnSuccessListener { documents ->
            for (document in documents) {
                val user = document.toObject(User::class.java)
                Log.d("Date Size", user.drinkHistory.size.toString());
                val drinkHistory = user.drinkHistory.filter {
                    val createdAt = it.createdAt.toDate()
                    createdAt.getDate() == date.getDate() &&
                            createdAt.getMonth() == date.getMonth() &&
                            createdAt.year == date.year
                }
                Log.d("Date DrinkHistory Filtered Size", drinkHistory.size.toString());
                onSuccess(ArrayList(drinkHistory))
            }
        }
    }
    fun deleteDrinkHistory(userId: String, date: Timestamp) {
        val userRef = db.collection("users").whereEqualTo("userId", userId)
        val targetDate = date.toDate()
        userRef.get().addOnSuccessListener { documents ->
            for (document in documents) {
                val user = document.toObject(User::class.java)
                val drinkHistory = user.drinkHistory.filter {
                    val createdAt = it.createdAt.toDate()
                    createdAt.getDate() != targetDate.getDate() ||
                            createdAt.getMonth() != targetDate.getMonth() ||
                            createdAt.year != targetDate.year
                }
                document.reference.update("drinkHistory", drinkHistory)
            }
        }
    }
    fun deleteItemDrinkHistory(userId: String, createdAt: Timestamp,onSuccess: () -> Unit) {
        val userRef = db.collection("users").whereEqualTo("userId", userId)
        userRef.get().addOnSuccessListener { documents ->
            for (document in documents) {
                val user = document.toObject(User::class.java)
                val drinkHistory = user.drinkHistory.filter {
                    it.createdAt != createdAt
                }
                document.reference.update("drinkHistory", drinkHistory)
            }
            onSuccess();
        }
    }





}