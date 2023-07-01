package com.example.healthcareapp.ui.activities.auth

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.healthcareapp.R
import com.example.healthcareapp.databinding.ActivityWelcomeBinding
import com.example.healthcareapp.ui.activities.MainActivity
import com.example.healthcareapp.viewmodels.AuthViewModel

import com.example.healthcareapp.viewmodels.AuthViewModel.Companion.isAuthenticated
import java.util.Calendar

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_welcome)
        setAuthenticateStateListener()
//        AuthViewModel.logout()
        AuthViewModel.Companion.checkUserLoggedIn()

        binding.btnAlreadyHaveAccount.setOnClickListener {
            val intent = Intent(this@WelcomeActivity, SignInActivity::class.java)
            startActivity(intent)
        }
        binding.btnSignUp.setOnClickListener {
            val intent = Intent(this@WelcomeActivity, SignUpActivity::class.java)
            startActivity(intent)
        }

        // Observe changes to the isAuthenticated field
    }

    private fun setAuthenticateStateListener() {
        isAuthenticated.observe(this) { isAuthenticated: Boolean ->
            if (isAuthenticated) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }

}