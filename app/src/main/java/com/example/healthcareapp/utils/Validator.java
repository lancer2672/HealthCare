package com.example.healthcareapp.utils;

import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;

import java.util.Objects;

public class Validator {
    public static boolean validatePassword(String password, String confirmPassword) {
        // Kiểm tra xem password có giống nhau không
        if (!Objects.equals(password, confirmPassword)) {
            return false;
        }
        return true;
    }

    public static boolean validateAge(String age) {
        // Kiểm tra xem tuổi có phải là số không
        try {
            Integer.parseInt(age);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static boolean validateGender(String gender) {
        // Kiểm tra xem giới tính có phải là "nam" hoặc "nữ" không
        if (!Objects.equals(gender.toLowerCase(), "nam") && !Objects.equals(gender.toLowerCase(), "nữ")) {
            return false;
        }
        return true;
    }
}
