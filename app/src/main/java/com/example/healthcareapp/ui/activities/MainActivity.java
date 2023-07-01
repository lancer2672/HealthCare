package com.example.healthcareapp.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.animation.ArgbEvaluator;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.healthcareapp.R;
import com.example.healthcareapp.data.models.MeditationModel;
import com.example.healthcareapp.databinding.ActivityMainBinding;
import com.example.healthcareapp.ui.activities.auth.WelcomeActivity;
import com.example.healthcareapp.ui.custom_components.Circle;
import com.example.healthcareapp.ui.fragments.Chat;
import com.example.healthcareapp.ui.fragments.Home;
import com.example.healthcareapp.ui.fragments.MeditationList;
import com.example.healthcareapp.ui.fragments.MeditationSession;
import com.example.healthcareapp.ui.fragments.UserProfile;
import com.example.healthcareapp.ui.fragments.WaterReminder;
import com.example.healthcareapp.ui.listeners.MeditationListener;
import com.example.healthcareapp.ui.listeners.ShowBottomDialog;
import com.example.healthcareapp.ui.listeners.ShowDialogListener;
import com.example.healthcareapp.utils.Formater;
import com.example.healthcareapp.viewmodels.AuthViewModel;
import com.example.healthcareapp.viewmodels.MeditationViewModel;
import com.example.healthcareapp.viewmodels.ReminderViewModel;
import com.example.healthcareapp.viewmodels.SurveyViewModel;
import com.google.android.material.navigation.NavigationBarView;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;

public class MainActivity extends AppCompatActivity implements ShowDialogListener, MeditationListener, ShowBottomDialog {
    private ActivityMainBinding binding;
    private SurveyViewModel surveyViewModel;
    private ReminderViewModel reminderViewModel;
    private MeditationViewModel  meditationViewModel;
    private  Home fHome;
    private  Chat fChat;
    private UserProfile fUser;
    private WaterReminder fWaterReminder;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        surveyViewModel = new ViewModelProvider(this).get(SurveyViewModel.class);
        meditationViewModel = new ViewModelProvider(this).get(MeditationViewModel.class);
        reminderViewModel = new ViewModelProvider(this).get(ReminderViewModel.class);
        sharedPreferences  = this.getSharedPreferences("time_preferences", MODE_PRIVATE);

        getReminderDataFromPref();
        setUpViewModel();
        setUpBinding();
        setInterval();

        showReminderNotification();
    }

    private void setInterval(){
        Timer timer = new Timer();
        Integer intervalTime = reminderViewModel.getIntervalTime();
        if (intervalTime == null) return;
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Calendar currentTime = Calendar.getInstance();
                currentTime.add(Calendar.MINUTE, intervalTime);
                Integer bedHour = reminderViewModel.getBedHour();
                Log.d("Interval bedtime", String.valueOf(bedHour));
                Integer bedMin = reminderViewModel.getBedMin();
                Integer wakeupHour = reminderViewModel.getWakeupHour();
                Integer wakeupMin = reminderViewModel.getWakeupMin();
                Calendar bedTime = Calendar.getInstance();
                bedTime.set(Calendar.HOUR_OF_DAY, bedHour);
                bedTime.set(Calendar.MINUTE, bedMin);

                Calendar wakeupTime = Calendar.getInstance();
                wakeupTime.set(Calendar.HOUR_OF_DAY, wakeupHour);
                wakeupTime.set(Calendar.MINUTE, wakeupMin);

                if (currentTime.after(bedTime) && currentTime.before(wakeupTime)) {
                    return;
                }

                showReminderNotification();
            }
        }, 0, intervalTime * 60*60 * 1000L);
    }

    private void showReminderNotification() {
        // Tạo kênh thông báo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "water_channel";
            CharSequence channelName = "Water Reminder";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, importance);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        // Tạo một đối tượng NotificationCompat.Builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "water_channel")
                .setSmallIcon(R.drawable.bell)
                .setContentTitle("HealthCare")
                .setContentText("It's time to drink water")
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        // Tạo một đối tượng Bitmap từ hình ảnh
        Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.logo_app_small);

        // Thêm hình ảnh vào thông báo
        builder.setStyle(new NotificationCompat.BigPictureStyle()
                .bigPicture(image)
                .bigLargeIcon((Bitmap) null));

        // Tạo một đối tượng NotificationManager
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Hiển thị thông báo
        notificationManager.notify(0, builder.build());
    }
    private void getReminderDataFromPref() {
        int bedTimeHour = sharedPreferences.getInt("bed_time_hour", -1);
        int bedTimeMinute = sharedPreferences.getInt("bed_time_minute", -1);
        int wakeUpHour = sharedPreferences.getInt("wake_up_hour", -1);
        int wakeUpMinute = sharedPreferences.getInt("wake_up_minute", -1);
        int intervalValue = sharedPreferences.getInt("interval", 4);
        String maxValue = sharedPreferences.getString("max_progress", "2000");
        if (bedTimeHour != -1) {
            reminderViewModel.setBedHour(bedTimeHour);
            reminderViewModel.setBedMin(bedTimeMinute);
        }
        if (wakeUpHour != -1) {
            reminderViewModel.setWakeupHour(wakeUpHour);
            reminderViewModel.setWakeupMin(wakeUpMinute);
        }
        reminderViewModel.setIntervalTime(intervalValue);
        reminderViewModel.getMaxProgress().setValue(Double.valueOf(maxValue));
    }

    public void setUpBinding(){
        binding.setViewmodel(meditationViewModel);
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
                    case R.id.menu_water_reminder:
                        fWaterReminder = new WaterReminder();
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment,fWaterReminder).commit();
                        return true;
                    default:
                }
                return false;
            }
        });
        binding.bottomNavBar.setSelectedItemId(R.id.menu_home);
        AuthViewModel.Companion.isAuthenticated().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(!aBoolean){
                    Intent i = new Intent(MainActivity.this, WelcomeActivity.class);
                    startActivity(i);
                }
            }
        });
    }
    public void setUpViewModel(){
        meditationViewModel.initPlayer(this);
        meditationViewModel.getAudios();
        meditationViewModel.setMeditationListenr(this);
        meditationViewModel.setShowBottomDialog(this);

        surveyViewModel.setShowDialogListener(this);
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
    //timer
    public void show() {
        Dialog bottomSheetDialog = new Dialog(this);
        bottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet);

        EditText edtTime = bottomSheetDialog.findViewById(R.id.edt_time);
        ImageView saveBtn = bottomSheetDialog.findViewById(R.id.save_time_btn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                meditationViewModel.setTimer(edtTime.getText().toString());
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.show();
        bottomSheetDialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        bottomSheetDialog.getWindow().getAttributes().windowAnimations = R.style.BottomSheetAnimation;
        bottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
    }
    @Override
    public void startSession(@NonNull MeditationModel meditationModel) {
        meditationViewModel.startMeditationSession(meditationModel);
        getSupportFragmentManager().beginTransaction().addToBackStack("stack").add(R.id.main_fragment, new MeditationSession()).commit();
    }

    @Override
    public void showResult(float value) {
        Dialog bottomSheetDialog = new Dialog(this);
        bottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        bottomSheetDialog.setContentView(R.layout.circle_result_component);


        CircularProgressIndicator circle = bottomSheetDialog.findViewById(R.id.circle);
        TextView tvResult = bottomSheetDialog.findViewById(R.id.tv_predicted_result);

        tvResult.setText(Formater.Companion.formatPredictedValue(value));

        // you can set max and current progress values individually
        circle.setMaxProgress(100.0);
        circle.setCurrentProgress(value*100);
        ArgbEvaluator evaluator = new ArgbEvaluator();
        int startColor = Color.GREEN;
        int endColor = Color.RED;
        float fraction = (float) (value*100 / circle.getMaxProgress());
        int color = (int) evaluator.evaluate(fraction, startColor, endColor);
        circle.setProgressColor(color);

        bottomSheetDialog.show();

        bottomSheetDialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
//        circle.triggerAnimation();
        bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        bottomSheetDialog.getWindow().getAttributes().windowAnimations = R.style.BottomSheetAnimation;
        bottomSheetDialog.getWindow().setGravity(Gravity.CENTER);

    }
}