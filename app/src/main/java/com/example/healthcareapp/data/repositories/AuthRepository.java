package com.example.healthcareapp.data.repositories;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.healthcareapp.utils.FirebaseAuthManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AuthRepository {
    private static volatile AuthRepository instance;

    public static AuthRepository getInstance() {
        if (instance == null) {
            synchronized (AuthRepository.class) {
                if (instance == null) {
                    instance = new AuthRepository();
                }
            }
        }
        return instance;
    }
    private FirebaseAuth firebaseAuth = FirebaseAuthManager.getInstance();

    public void signInWithEmail(String email, String password, OnCompleteListener<AuthResult> onCompleteListener) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(onCompleteListener);
    }
}
