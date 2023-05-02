package com.example.healthcareapp.utils;

import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;

public class Validator {
    public static String validateEmail(String email) {
        if (TextUtils.isEmpty(email)) {
            return "Email không được để trống";
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return "Email không hợp lệ";
        } else {
            return null;
        }
    }

    public static String validatePassword(String password) {
        if (TextUtils.isEmpty(password)) {
            return "Mật khẩu không được để trống";
        } else if (password.length() < 6) {
            return "Mật khẩu phải chứa ít nhất 6 ký tự";
        } else {
            return null;
        }
    }

    public static String validateAge(String age) {
        if (age.isEmpty()) {
            return "Tuổi không được để trống";
        } else if (Integer.parseInt(age) < 0) {
            return "Tuổi không hợp lệ";
        } else if (Integer.parseInt(age) < 14) {
            return "Bạn chưa đủ 14 tuổi";
        } else {
            return null;
        }
    }

    public static String validateIsEmpty(EditText editText, String errorMessage) {
        if (TextUtils.isEmpty(editText.getText().toString().trim())) {
            return errorMessage;
        } else {
            return null;
        }
    }

    public static String validateName(String name) {
        if (name.isEmpty()) {
            return "Tên không được để trống";
        } else if (!name.matches("^[a-zA-Z0-9 ]+$")){
            return "Tên không được chứa kí tự đặc biệt";
        } else {
            return null;
        }
    }

}
