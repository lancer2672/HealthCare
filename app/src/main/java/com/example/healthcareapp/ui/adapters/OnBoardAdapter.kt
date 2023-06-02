package com.example.healthcareapp.ui.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.healthcareapp.R

class OnBoardAdapter(private val list:List<String>) : RecyclerView.Adapter<OnBoardAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.on_board_item, parent,false);
        return ViewHolder(view);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val text = list[position];
        holder.tv.text = text;
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
         var tv:TextView
        init {
            tv = itemView.findViewById(R.id.on_board_tv);
        }
    }


}