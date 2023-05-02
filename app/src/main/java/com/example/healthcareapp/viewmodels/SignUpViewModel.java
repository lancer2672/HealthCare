package com.example.healthcareapp.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.NavController;

import com.example.healthcareapp.data.repositories.AuthRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class SignUpViewModel extends ViewModel {
    public MutableLiveData<String> email = new MutableLiveData<>("");
    public MutableLiveData<String> password = new MutableLiveData<>("");
    public MutableLiveData<String> confirmPassword = new MutableLiveData<>("");
    public MutableLiveData<String> age = new MutableLiveData<>("");
    public MutableLiveData<String> name = new MutableLiveData<>("");
    private NavController navController;
    public MutableLiveData<Boolean> isLoading = new MutableLiveData<Boolean>(false);
    public MutableLiveData<Boolean> isCreateSucceeded = new MutableLiveData<Boolean>(false);
    public NavController getNavController(){
        return navController;
    }
    public void setNavController (NavController nav){
            this.navController = nav;
    }
    public void register() {
        isLoading.setValue(true);
        AuthRepository.getInstance().signUpWithEmail(email.getValue(), password.getValue(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    isLoading.setValue(false);
                    isCreateSucceeded.setValue(true);
                } else {
                    isLoading.setValue(false);
                    isCreateSucceeded.setValue(false);
                }
            }
        });
    }
}
