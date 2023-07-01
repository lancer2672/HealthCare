package com.example.healthcareapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.healthcareapp.R
import com.example.healthcareapp.data.models.DrinkHistoryModel
import com.example.healthcareapp.data.models.MeditationHistory
import com.example.healthcareapp.utils.Formater
import com.example.healthcareapp.viewmodels.MeditationViewModel
import com.example.healthcareapp.viewmodels.ReminderViewModel
import com.squareup.picasso.Picasso


class ListRecordAdapter(
    private val context: Context,
    private val reminderViewModel: ReminderViewModel,
    private val list: List<DrinkHistoryModel> ):
    RecyclerView.Adapter<ListRecordAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_record,parent,false);
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position];
        holder.bind(item);
    }
    override fun getItemCount(): Int {
        return list.size
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private var createdAt: TextView;
        private var value: TextView;
        private var deleteBtn: ImageView;

        init {
            createdAt = itemView.findViewById(R.id.createdAt_record)
            value = itemView.findViewById(R.id.value_record)
            deleteBtn = itemView.findViewById(R.id.delete_record_btn);
        }

        fun bind(drinkHistoryModel: DrinkHistoryModel){
            val hour =  drinkHistoryModel.createdAt.toDate().hours
            val min =  drinkHistoryModel.createdAt.toDate().minutes
            value.text = "${drinkHistoryModel.value} ml"
            createdAt.text = Formater.formatTime(hour, min);
            deleteBtn.visibility = View.GONE
            deleteBtn.setOnClickListener {
                reminderViewModel.deleteItemDrinkHistory(drinkHistoryModel.createdAt)
            }
        }
    }
}