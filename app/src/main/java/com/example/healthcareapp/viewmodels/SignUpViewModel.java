package com.example.healthcareapp.viewmodels;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.NavController;

import com.example.healthcareapp.data.models.User;
import com.example.healthcareapp.data.repositories.AuthRepository;
import com.example.healthcareapp.data.repositories.UserRepository;
import com.example.healthcareapp.utils.Validator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Objects;

public class SignUpViewModel extends ViewModel {
    public MutableLiveData<String> email = new MutableLiveData<>("");
    public MutableLiveData<String> password = new MutableLiveData<>("");
    public MutableLiveData<String> confirmPassword = new MutableLiveData<>("");
    public MutableLiveData<String> age = new MutableLiveData<>("");
    public MutableLiveData<String> gender = new MutableLiveData<>("");
    public MutableLiveData<String> name = new MutableLiveData<>("");
    public ObservableField<String> error = new ObservableField<>("");
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
        // Kiểm tra thông tin người dùng nhập vào
        if (!Validator.validatePassword(password.getValue(), confirmPassword.getValue())) {
            error.set("Password and confirm password do not match");
            return;
        }
        if (!Validator.validateAge(age.getValue())) {
            error.set("Age must be a number");
            return;
        }
        if (!Validator.validateGender(gender.getValue())) {
            error.set("Gender is invalid");
            return;
        }

        // Nếu thông tin người dùng nhập vào hợp lệ, tiếp tục thực hiện đăng ký
        isLoading.setValue(true);
        User user = new User("",
                Objects.requireNonNull(name.getValue()),
                Objects.requireNonNull(gender.getValue()),
                Objects.requireNonNull(age.getValue()),
                "", new ArrayList<>(), new ArrayList<>());

        AuthRepository.getInstance().signUpWithEmail(email.getValue(), password.getValue(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    user.setUserId(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
                    Objects.requireNonNull(UserRepository.Companion.getInstance()).createUser(user);
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
