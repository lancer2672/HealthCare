package com.example.healthcareapp.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.healthcareapp.R;
import com.example.healthcareapp.databinding.ActivityMainBinding;
import com.example.healthcareapp.ui.custom_components.Circle;
import com.example.healthcareapp.ui.custom_components.CircleAngleAnimation;
import com.example.healthcareapp.ui.fragments.Chat;
import com.example.healthcareapp.ui.fragments.Home;
import com.example.healthcareapp.ui.fragments.UserProfile;
import com.example.healthcareapp.ui.listeners.ShowDialogListener;
import com.example.healthcareapp.utils.Formater;
import com.example.healthcareapp.viewmodels.SurveyViewModel;
import com.google.android.material.navigation.NavigationBarView;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements ShowDialogListener {
    private ActivityMainBinding binding;
    private SurveyViewModel surveyViewModel;
    private  Home fHome;
    private  Chat fChat;
    private UserProfile fUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        surveyViewModel = new ViewModelProvider(this).get(SurveyViewModel.class);
        surveyViewModel.setShowDialogListener(this);
        binding.bottomNavBar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_home:
                         fHome = new Home();
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment,fHome).commit();
                        return true;
                    case R.id.menu_chat:
                         fChat = new Chat();
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment,fChat).commit();
                        return true;
                    case R.id.menu_profile:
                         fUser = new UserProfile();
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment,fUser).commit();
                        return true;
                    case R.id.menu_feedback:
                        return true;
                    default:
                }
                return false;
            }
        });
        binding.bottomNavBar.setSelectedItemId(R.id.menu_home);
        surveyViewModel.getPredictedResult().observe(this, new Observer<Float>() {
            @Override
            public void onChanged(Float aFloat) {
                if(aFloat >0f && aFloat <1f) {
                    showResult(aFloat);
                }
            }
        });
    }
    @Override
    public void showResult(float value) {
        Dialog bottomSheetDialog = new Dialog(this);
        bottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        bottomSheetDialog.setContentView(R.layout.circle_result_component);

        Circle circle = bottomSheetDialog.findViewById(R.id.circle);
        TextView tvResult = bottomSheetDialog.findViewById(R.id.tv_predicted_result);
        circle.setAngle(value);
        tvResult.setText(Formater.Companion.formatPredictedValue(value));
        circle.triggerAnimation();
//        bottomSheetDialog.setOnShowListener(new DialogInterface.OnShowListener() {
//            @Override
//            public void onShow(DialogInterface dialog) {
//                CircleAngleAnimation animation = new CircleAngleAnimation(circle, (int) (value*360));
//                animation.setDuration(1000);
//                circle.startAnimation(animation);
//            }
//        });
        bottomSheetDialog.show();

        bottomSheetDialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
//        circle.triggerAnimation();
        bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        bottomSheetDialog.getWindow().getAttributes().windowAnimations = R.style.BottomSheetAnimation;
        bottomSheetDialog.getWindow().setGravity(Gravity.CENTER);
        circle.setValue(value);
    }
}