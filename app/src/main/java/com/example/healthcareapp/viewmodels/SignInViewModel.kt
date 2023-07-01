package com.example.healthcareapp.viewmodels

import android.database.Observable
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.healthcareapp.data.repositories.AuthRepository
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.runBlocking

class SignInViewModel : ViewModel() {
    val email = MutableLiveData("")
    val password = MutableLiveData("")
    val error = ObservableField<String>("")
    val isLoading = MutableLiveData(false)
    val isAuthenticated = MutableLiveData(false)

    fun login() {
        AuthRepository.getInstance().signInWithEmail(email.value, password.value,
            OnCompleteListener<AuthResult> { task ->
                if (task.isSuccessful) {
                    val id = FirebaseAuth.getInstance().currentUser?.uid
                    if (id != null) {
                        runBlocking{
                            AuthViewModel.getUserFromDB(id)
                        }
                    }
                    isAuthenticated.value = true
                    Log.d("AUTHENTICATION", "SUCCESS")
                } else {
                    isAuthenticated.value = false
                    Log.d("AUTHENTICATION", "FAILED")
                }
            })
    }
}
