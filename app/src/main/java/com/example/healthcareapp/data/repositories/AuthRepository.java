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

    public void signInWithEmail(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
// Sign in success, update UI with the signed-in user's information
                            Log.d("AUTHENTICATION", "signInWithEmail:success");
                        } else {
// If sign in fails, display a message to the user.
                            Log.w("AUTHENTICATION", "signInWithEmail:failure", task.getException());
                        }
                    }
                });
    }
}
