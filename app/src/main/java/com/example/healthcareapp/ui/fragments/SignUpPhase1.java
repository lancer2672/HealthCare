package com.example.healthcareapp.ui.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.healthcareapp.R;
import com.example.healthcareapp.databinding.FragmentSignUpPhase1Binding;
import com.example.healthcareapp.viewmodels.SignUpViewModel;


public class SignUpPhase1 extends Fragment {

    private SignUpViewModel signUpViewModel ;
    private FragmentSignUpPhase1Binding binding;
    public SignUpPhase1() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up_phase1, container, false);
        signUpViewModel = new ViewModelProvider(requireActivity()).get(SignUpViewModel.class);
        binding.setViewmodel(signUpViewModel);
        binding.btnContinueSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpViewModel.getNavController().navigate(R.id.action_signUpPhase1_to_signUpPhase2);
            }
        });
        return binding.getRoot();
    }
}