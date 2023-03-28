package com.example.healthcareapp.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.healthcareapp.R;
import com.example.healthcareapp.databinding.ActivityLoginBinding;
import com.example.healthcareapp.ui.viewmodels.LoginViewModel;

public class Login extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private LoginViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
}