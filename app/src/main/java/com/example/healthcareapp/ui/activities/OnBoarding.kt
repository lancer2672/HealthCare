package com.example.healthcareapp.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.healthcareapp.R
import com.example.healthcareapp.databinding.ActivityOnBoardingBinding
import com.example.healthcareapp.ui.activities.auth.WelcomeActivity
import com.example.healthcareapp.ui.adapters.OnBoardAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class OnBoarding : AppCompatActivity() {
    private lateinit var adapter: OnBoardAdapter
    private lateinit var binding: ActivityOnBoardingBinding
    private lateinit var arr:List<String>;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arr = listOf<String>(getString(R.string.on_boarding_1_content),getString(R.string.on_boarding_2_content));
        adapter = OnBoardAdapter(arr);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_on_boarding);
        binding.onBoardViewPager.adapter = adapter
        TabLayoutMediator(binding.onBoardTabView,binding.onBoardViewPager){tab, position ->
            tab.text ="Tab $position";
        }.attach();
        binding.onBoardTabView.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if(tab?.position != 0)
                {
                    binding.onBoardBackBtn.visibility = View.VISIBLE;
                }
                else {
                    binding.onBoardBackBtn.visibility = View.INVISIBLE;
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
        binding.onBoardNextBtn.setOnClickListener {
            val currentIndex = binding.onBoardViewPager.currentItem;
            binding.onBoardViewPager.currentItem = if(currentIndex < arr.size) currentIndex + 1 else  currentIndex
            if(currentIndex == arr.size -1){
                val intent = Intent(binding.root.context, WelcomeActivity::class.java);
                startActivity(intent);
            }
        }
        binding.onBoardBackBtn.setOnClickListener {
            val currentIndex = binding.onBoardViewPager.currentItem;
            binding.onBoardViewPager.currentItem = if(currentIndex >0 ) currentIndex - 1 else  currentIndex

        }
    }
}