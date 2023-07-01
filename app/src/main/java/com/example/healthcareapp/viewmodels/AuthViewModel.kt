package com.example.healthcareapp.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.healthcareapp.data.models.User
import com.example.healthcareapp.data.repositories.UserRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.runBlocking

class AuthViewModel : ViewModel() {
    companion object {
        var isAuthenticated = MutableLiveData(false)
        private var user: User? = null
        fun checkUserLoggedIn() {
            if (FirebaseAuth.getInstance().currentUser != null) {
                // The user is signed in
                isAuthenticated.value = (true)
                runBlocking {
                    FirebaseAuth.getInstance().currentUser?.uid?.let { getUserFromDB(it) };
                }
                Log.d("AUTHENTICATION", "User logged in")
            } else {
                // The user is not signed in
                isAuthenticated.value = (false)
                Log.d("AUTHENTICATION", "User didn't logged in")
            }
        }

        suspend fun getUserFromDB(userId: String, onFailure : (() -> Unit)? = null) {
            UserRepository.instance?.getUserFromDb(userId ,{ u ->
                user = u
            }){
                if (onFailure != null) {
                    onFailure()
                }
            }
        }

        fun getUser(): User? {
            return user
        }
        fun logout() {
            FirebaseAuth.getInstance().signOut()
            isAuthenticated.value = false
            user = null
            Log.d("AUTHENTICATION", "User logged out")
        }
    }
}