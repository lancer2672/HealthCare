package com.example.healthcareapp.ui.activities.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.Observable;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.healthcareapp.R;
import com.example.healthcareapp.databinding.ActivityWelcomeBinding;
import com.example.healthcareapp.ui.activities.MainActivity;
import com.example.healthcareapp.viewmodels.AuthViewModel;

public class WelcomeActivity extends AppCompatActivity {
    private ActivityWelcomeBinding binding;
    private AuthViewModel authViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_welcome);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        setAuthenticateStateListener();
        authViewModel.checkUserLoggedIn();

        binding.btnAlreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this,SignInActivity.class);
                startActivity(intent);
            }
        });
        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });
        // Observe changes to the isAuthenticated field

    }
    private void setAuthenticateStateListener(){
        authViewModel.isAuthenticated.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                // Get the current value of the isAuthenticated field
                boolean isAuthenticated = authViewModel.isAuthenticated.get();
                // Update the UI based on the authentication state
                if (isAuthenticated) {
                    Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                    Log.d("AUTHENTICATION","Navigating to main screen");
                    startActivity(intent);
                } else {
                }
            }
        });
    }

}