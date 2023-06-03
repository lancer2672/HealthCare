package com.example.healthcareapp.viewmodels;

import android.util.Log;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.healthcareapp.utils.FirebaseAuthManager;
import com.google.firebase.auth.FirebaseAuth;

public class AuthViewModel extends ViewModel {
    public ObservableField<Boolean> isAuthenticated = new ObservableField<>(false);

    public void checkUserLoggedIn(){
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            // The user is signed in
            isAuthenticated.set(true);
            Log.d("AUTHENTICATION", "User logged in");
        } else {
            // The user is not signed in
            isAuthenticated.set(false);
            Log.d("AUTHENTICATION", "User didn't logged in");

        }
    }
    public void signOut(){
        FirebaseAuth.getInstance().signOut();
    }
}
