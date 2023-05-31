package com.example.healthcareapp.viewmodels;

import android.database.Observable;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.healthcareapp.data.repositories.AuthRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class SignInViewModel extends ViewModel {
    public MutableLiveData<String> email = new MutableLiveData<>("");
    public MutableLiveData<String> password = new MutableLiveData<>("");
    public MutableLiveData<Boolean> isLoading = new MutableLiveData<Boolean>(false);
    public MutableLiveData<Boolean> isAuthenticated = new MutableLiveData<Boolean>(false);
    public void login() {
        AuthRepository.getInstance().signInWithEmail(email.getValue(), password.getValue(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    isAuthenticated.setValue(true); // set trạng thái đăng nhập thành công và emit event để Activity có thể observe
                } else {
                    isAuthenticated.setValue(false); // set trạng thái đăng nhập thất bại và emit event để Activity có thể observe
                }
            }
        });
    }
}
