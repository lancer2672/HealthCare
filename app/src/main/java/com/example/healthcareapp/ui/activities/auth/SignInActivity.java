package com.example.healthcareapp.ui.activities.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.healthcareapp.R;
import com.example.healthcareapp.databinding.ActivitySignInBinding;

import com.example.healthcareapp.ui.activities.SurveyActivity;
import com.example.healthcareapp.viewmodels.SignInViewModel;

public class SignInActivity extends AppCompatActivity  {
    private SignInViewModel signInViewModel;
    private ActivitySignInBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_sign_in);
        signInViewModel = new ViewModelProvider(this).get(SignInViewModel.class);
        binding.setViewmodel(signInViewModel);
        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInViewModel.login();
            }
        });
        signInViewModel.isLoading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                // Handle isLoading value
            }
        });

        signInViewModel.isAuthenticated.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isAuthenticated) {
                if(isAuthenticated){
                    Intent intent  = new Intent(SignInActivity.this, SurveyActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}