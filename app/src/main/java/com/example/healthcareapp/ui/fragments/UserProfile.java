package com.example.healthcareapp.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.example.healthcareapp.R;
import com.example.healthcareapp.databinding.FragmentUserProfileBinding;
import com.example.healthcareapp.viewmodels.SurveyViewModel;

import java.util.Objects;


public class UserProfile extends Fragment {
    private FragmentUserProfileBinding binding;
    private ActivityHistory activityHistory;
    private Chart chart;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_user_profile,container,false);
        chart = new Chart();
        activityHistory = new ActivityHistory();
        binding.radGr.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_button1:
                        inflateActivityHistoryFragment();
                        // Handle click on "Stress" radio button
                        break;
                    case R.id.radio_button2:
                        inflateChartFragment();
                        // Handle click on "Anxiety" radio button
                        break;
                }

            }
        });
        binding.radGr.check(R.id.radio_button1);

        return binding.getRoot();
    }
    private void inflateActivityHistoryFragment() {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Replace the current fragment container with the ActivityHistory fragment
        fragmentTransaction.replace(R.id.fragment_container, activityHistory);
        fragmentTransaction.commit();
    }
    private void inflateChartFragment() {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Replace the current fragment container with the ActivityHistory fragment
        fragmentTransaction.replace(R.id.fragment_container, chart);
        fragmentTransaction.commit();
    }
}