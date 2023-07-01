package com.example.healthcareapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.healthcareapp.R
import com.example.healthcareapp.data.models.MeditationModel
import com.example.healthcareapp.viewmodels.MeditationViewModel
import com.google.android.material.button.MaterialButton
import com.squareup.picasso.Picasso


class MeditationAdapter(
    private val context: Context,
    private val meditationViewModel:MeditationViewModel,
    private val list: List<MeditationModel> ):
    RecyclerView.Adapter<MeditationAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.meditation_item,parent,false);
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position];
//        holder.itemView.setOnClickListener{
////            playerViewModel.getPlaySongListener()?.playSong(position, song)
////            song.mediaItemIndex?.let { it1    -> playerViewModel.getPlaySongListener()?.playSong(it1) };
//        }
        holder.bind(item);
    }
    override fun getItemCount(): Int {
        return list.size
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private var title: TextView;
        private var image: ImageView;
        private var startBtn: MaterialButton
        init {
            title = itemView.findViewById(R.id.tv_title_meditation_item)
            image = itemView.findViewById(R.id.img_meditation_item);
            startBtn= itemView.findViewById(R.id.btn_start_meditation_item);
        }

        fun bind(meditationModel: MeditationModel){
            Picasso.get().load(meditationModel.imageUri).into(image)
            title.text = meditationModel.name
            startBtn.setOnClickListener {
                meditationViewModel.meditationListenr?.startSession(meditationModel);
            }
        }
    }
}