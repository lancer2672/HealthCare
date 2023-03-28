package com.example.healthcareapp.ui.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RegisterViewModel extends ViewModel {
    public MutableLiveData<String> _email;
    public MutableLiveData<String> _password;
    public MutableLiveData<String> _name;
    public MutableLiveData<String> _age;
    public MutableLiveData<String> _gender;

    public MutableLiveData<String> getAge() {
        return _age;
    }

    public MutableLiveData<String> getName() {
        return _name;
    }

    public void setName(MutableLiveData<String> _name) {
        this._name = _name;
    }

    public void setAge(MutableLiveData<String> _age) {
        this._age = _age;
    }

    public MutableLiveData<String> getGender() {
        return _gender;
    }

    public void setGender(MutableLiveData<String> _gender) {
        this._gender = _gender;
    }

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
    public void onRegisterClicked(){

    }
}
