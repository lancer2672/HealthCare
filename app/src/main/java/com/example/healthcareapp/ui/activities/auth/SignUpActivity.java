package com.example.healthcareapp.ui.activities.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.content.Intent;
import android.os.Bundle;

import com.example.healthcareapp.R;
import com.example.healthcareapp.databinding.ActivitySignUpBinding;
import com.example.healthcareapp.ui.activities.MainActivity;
import com.example.healthcareapp.viewmodels.SignInViewModel;
import com.example.healthcareapp.viewmodels.SignUpViewModel;

public class SignUpActivity extends AppCompatActivity {
    private SignUpViewModel signUpViewModel;
    private ActivitySignUpBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_sign_up);
        signUpViewModel = new ViewModelProvider(this).get(SignUpViewModel.class);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        signUpViewModel.setNavController(navController);
        signUpViewModel.isCreateSucceeded.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isCreateSucceeded) {
                if(isCreateSucceeded){
                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });

        binding.setViewmodel(signUpViewModel);
    }
}