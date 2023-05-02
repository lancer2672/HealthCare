package com.example.healthcareapp.utils;

interface AuthListener {
    void onSuccess();
    void onFailure(String message);
}