package com.example.healthcareapp.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.healthcareapp.R;
import com.example.healthcareapp.databinding.ActivityMainBinding;
import com.example.healthcareapp.ui.fragments.Chat;
import com.example.healthcareapp.ui.fragments.Home;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.bottomNavBar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_home:
                        Home fHome = new Home();
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment,fHome).commit();
                        break;
                    case R.id.menu_chat:
                        Chat fChat = new Chat();
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment,fChat).commit();
                        break;
                    case R.id.menu_profile:
                        break;
                    case R.id.menu_feedback:
                        break;
                    default:
                }
                return false;
            }
        });
    }
}