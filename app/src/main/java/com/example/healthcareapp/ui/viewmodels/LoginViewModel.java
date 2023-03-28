package com.example.healthcareapp.ui.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LoginViewModel extends ViewModel {
    public MutableLiveData<String> _email;
    public MutableLiveData<String> _password;

    public MutableLiveData<String> getEmail() {
        return _email;
    }

    public void setEmail(String email) {
        this._email.setValue(email);
    }

    public MutableLiveData<String> getPassword() {
        return _password;
    }

    public void setPassword(String password) {
        this._password.setValue(password);
    }
    public void onLoginClicked(){

    }

}
