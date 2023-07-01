package com.example.healthcareapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.healthcareapp.R
import com.example.healthcareapp.data.models.MeditationHistory
import com.example.healthcareapp.viewmodels.MeditationViewModel
import com.google.android.material.button.MaterialButton
import com.squareup.picasso.Picasso


class MeditationHistoryAdapter(
    private val context: Context,
    private val meditationViewModel: MeditationViewModel,
    private val list: List<MeditationHistory> ):
    RecyclerView.Adapter<MeditationHistoryAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_history_activity,parent,false);
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
        private var title: TextView;
        private var createdAt: TextView;
        private var image: ImageView;

        init {
            title = itemView.findViewById(R.id.activity_title)
            createdAt = itemView.findViewById(R.id.activity_time)
            image = itemView.findViewById(R.id.activity_img);
        }

        fun bind(meditationHistoryModel: MeditationHistory){
            Picasso.get().load(meditationHistoryModel.imageUri).into(image)
            title.text = meditationHistoryModel.name
            createdAt.text = meditationHistoryModel.createdAt
        }
    }
}