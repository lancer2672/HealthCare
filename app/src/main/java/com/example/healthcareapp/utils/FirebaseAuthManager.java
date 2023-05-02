package com.example.healthcareapp.utils;

import com.example.healthcareapp.data.repositories.AuthRepository;
import com.google.firebase.auth.FirebaseAuth;

public class FirebaseAuthManager {
    private static FirebaseAuth instance = null;

    public static synchronized FirebaseAuth getInstance() {
        if (instance == null) {
            instance = FirebaseAuth.getInstance();
        }
        return instance;
    }
}

