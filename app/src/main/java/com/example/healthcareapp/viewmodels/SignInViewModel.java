package com.example.healthcareapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.healthcareapp.data.repositories.AuthRepository;

public class SignInViewModel extends ViewModel {
    private MutableLiveData<String> _email;
    private MutableLiveData<String> _password;
    public void setEmail(String email) {
        this._email.setValue(email);
    }

    public LiveData<String> getEmail() {
        return _email;
    }

    public void setPassword(String password) {
        this._password.setValue(password);
    }

    public LiveData<String> getPassword() {
        return _password;
    }

    public void login(){
        AuthRepository.getInstance().signInWithEmail(_email.getValue(),_password.getValue());
    }
}
