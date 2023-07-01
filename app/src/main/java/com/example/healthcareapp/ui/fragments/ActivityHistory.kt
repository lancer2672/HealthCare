package com.example.healthcareapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.example.healthcareapp.R
import com.example.healthcareapp.databinding.FragmentActivityHistoryBinding
import com.example.healthcareapp.databinding.FragmentChartBinding
import com.example.healthcareapp.ui.adapters.MeditationHistoryAdapter
import com.example.healthcareapp.viewmodels.AuthViewModel
import com.example.healthcareapp.viewmodels.MeditationViewModel
import com.example.healthcareapp.viewmodels.SurveyViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ActivityHistory.newInstance] factory method to
 * create an instance of this fragment.
 */
class ActivityHistory : Fragment() {
    private val meditationViewModel : MeditationViewModel by activityViewModels()
    private lateinit var binding : FragmentActivityHistoryBinding;
    private lateinit var  adapter: MeditationHistoryAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_activity_history, container, false)
        if(AuthViewModel.getUser()!!.meditationHistory.size !=0){
            binding.activityEmpty.visibility = View.GONE
        }
        adapter = MeditationHistoryAdapter(requireActivity(),meditationViewModel,AuthViewModel.getUser()!!.meditationHistory)
        binding.recyclerviewActivity.adapter = adapter
        return binding.root
    }

}