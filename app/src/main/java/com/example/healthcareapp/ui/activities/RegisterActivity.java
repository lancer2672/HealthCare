package com.example.healthcareapp.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.healthcareapp.R;
import com.example.healthcareapp.databinding.ActivityRegisterBinding;
import com.example.healthcareapp.ui.viewmodels.RegisterViewModel;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private RegisterViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }
}