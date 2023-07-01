package com.example.healthcareapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.healthcareapp.R
import com.example.healthcareapp.databinding.FragmentMeditationSessionsBinding
import com.example.healthcareapp.ui.adapters.MeditationAdapter
import com.example.healthcareapp.viewmodels.MeditationViewModel

class MeditationList : Fragment() {

    private lateinit var binding: FragmentMeditationSessionsBinding
    private val meditationViewModel :MeditationViewModel by activityViewModels()
    private lateinit var adapter: MeditationAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_meditation_sessions, container, false)
        adapter = MeditationAdapter(requireActivity(),meditationViewModel,meditationViewModel.listAudios);
        binding.recMediation.adapter = adapter
        return binding.root
    }
}