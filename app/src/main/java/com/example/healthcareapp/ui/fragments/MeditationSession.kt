package com.example.healthcareapp.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.example.healthcareapp.R
import com.example.healthcareapp.data.models.MeditationHistory
import com.example.healthcareapp.databinding.FragmentMeditationSessionBinding
import com.example.healthcareapp.utils.Formater
import com.example.healthcareapp.viewmodels.MeditationViewModel
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.TimeBar
import java.sql.Timestamp

class MeditationSession : Fragment() {

    private lateinit var binding: FragmentMeditationSessionBinding
    private val meditationViewModel : MeditationViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_meditation_session, container, false)

        // Inflate the layout for this fragment
        binding.timeBar.setDuration(meditationViewModel.currentItem.value?.duration ?: 0)
        binding.durationTxt.text = Formater.formatDuration(meditationViewModel.currentItem.value?.duration
            ?: 0)
        binding.timeBar.setPosition(meditationViewModel.player.currentPosition);
        binding.meditationTitle.text = meditationViewModel.currentItem.value?.name ?: ""
        binding.createTimer.setOnClickListener {
            meditationViewModel.showBottomDialog
            meditationViewModel.showBottomDialog?.show()
        }

        setUpMusicPlayer();

        return binding.root
    }

    private fun setUpMusicPlayer() {
        // Thiết lập playerControlView với player từ ViewModel
        binding.playerControlView.player = meditationViewModel.player

        binding.playerControlView.player?.addListener(object : Player.Listener {

            // Xử lý sự kiện khi chuyển media item trong player
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {

                super.onMediaItemTransition(mediaItem, reason)
            }
            // Xử lý sự kiện khi trạng thái isPlaying thay đổi
            override fun onIsPlayingChanged(isPlaying: Boolean) {
            }

            // Xử lý sự kiện khi repeatMode thay đổi
            override fun onRepeatModeChanged(repeatMode: Int) {
                super.onRepeatModeChanged(repeatMode)
            }

            // Xử lý sự kiện khi trạng thái playWhenReady thay đổi
            override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
                super.onPlayWhenReadyChanged(playWhenReady, reason)
            }
        })

        // Xử lý sự kiện khi người dùng chạm vào timeBar
        binding.timeBar.addListener(object : TimeBar.OnScrubListener {
            override fun onScrubMove(timeBar: TimeBar, position: Long) {
            }
            override fun onScrubStart(timeBar: TimeBar, position: Long) {
            }
            override fun onScrubStop(timeBar: TimeBar, position: Long, canceled: Boolean) {
                meditationViewModel.player.seekTo(position)
                binding.timeBar.setPosition(position);
                binding.currentPositionTxt.text = Formater.formatDuration(position);
            }
        })
        //set timebar value keep updated
        binding.playerControlView.setProgressUpdateListener { position, bufferedPosition ->
            if(meditationViewModel.player.isPlaying){
                binding.timeBar.setPosition(position)
                binding.timeBar.setBufferedPosition(bufferedPosition)
                binding.currentPositionTxt.text = Formater.formatDuration(position);
                meditationViewModel.previousPosition = meditationViewModel.currentPosition
                meditationViewModel.currentPosition = position
                Log.d("Position Prev", meditationViewModel.previousPosition.toString());
                Log.d("Position currentPosition", meditationViewModel.currentPosition.toString());
            }
        }
    }

}