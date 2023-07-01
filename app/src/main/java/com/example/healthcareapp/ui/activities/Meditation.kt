package com.example.healthcareapp.ui.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.healthcareapp.R
import com.example.healthcareapp.data.models.MeditationModel
import com.example.healthcareapp.databinding.ActivityMeditationBinding
import com.example.healthcareapp.utils.Formater
import com.example.healthcareapp.viewmodels.MeditationViewModel
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.TimeBar

class Meditation : AppCompatActivity() {
    private lateinit var binding: ActivityMeditationBinding;
    private lateinit var viewModel: MeditationViewModel;
    override fun onCreate(savedInstanceState: Bundle?) {
        val type = intent.getStringExtra("type")
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MeditationViewModel::class.java];
        binding = DataBindingUtil.setContentView(this,R.layout.activity_meditation);

        viewModel.initPlayer(this);
        viewModel.getAudios()
        getAudiosFromType(type);

        setUpMusicPlayer();

    }

    private fun getAudiosFromType(type:String?){
        when (type) {
            "sleep" -> {
                binding.meditationTitle.text = "Meditation for sleeping"
                val list = viewModel.listAudios.filter {
                    it.type == "sleep"
                }
                if(list.isNotEmpty())
                    viewModel.startMeditationSession(list[0]);
            }
            "beginner" -> {
                binding.meditationTitle.text = "Meditation for beginner"
                val list = viewModel.listAudios.filter {
                    it.type == "beginner"
                }
                if(list.isNotEmpty())
                    viewModel.startMeditationSession(list[0]);
            }
            else -> {
                // Handle the case where param is neither "sleep" nor "beginner"
            }
        }
        binding.timeBar.setDuration(viewModel.currentItem.value?.duration ?: 0)
        binding.durationTxt.text = Formater.formatDuration(viewModel.currentItem.value?.duration
            ?: 0)
        binding.timeBar.setPosition(viewModel.player.currentPosition);
    }
    private fun setUpMusicPlayer() {
        // Thiết lập playerControlView với player từ ViewModel
        binding.playerControlView.player = viewModel.player

        binding.playerControlView.player?.addListener(object : Player.Listener {
            // Xử lý sự kiện khi chuyển media item trong player
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                Log.i("PlayerEvents", "onMediaItemTransition Song Duration ${viewModel.player.duration}")
                super.onMediaItemTransition(mediaItem, reason)
//                binding.durationTxt.text = Formater.formatDuration(viewModel.currentItem.value?.duration ?: 0)
//                viewModel.currentItem.value?.let { disFragment.setImage(it.imageUri) };
            }
            // Xử lý sự kiện khi trạng thái isPlaying thay đổi
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                Log.i("PlayerEvents", "OnIsPlaying ${isPlaying.toString()}")
                if (isPlaying) {
//                    disFragment.resumeAnimation();
                } else {
//                    disFragment.stopAnimation()
                }
//                viewModel.getActionPlayerListener()?.playCLicked()
            }

            // Xử lý sự kiện khi repeatMode thay đổi
            override fun onRepeatModeChanged(repeatMode: Int) {
                super.onRepeatModeChanged(repeatMode)
            }

            // Xử lý sự kiện khi trạng thái playWhenReady thay đổi
            override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
                Log.i("PlayerEvents", "onPlayWhenReadyChanged ${playWhenReady.toString()}")

                super.onPlayWhenReadyChanged(playWhenReady, reason)
                if (playWhenReady) {
//                    disFragment.resumeAnimation()
                    viewModel.player.play()
                } else {
//                    disFragment.stopAnimation()
                }
            }
        })

        // Xử lý sự kiện khi người dùng chạm vào timeBar
        binding.timeBar.addListener(object : TimeBar.OnScrubListener {
            override fun onScrubMove(timeBar: TimeBar, position: Long) {
            }
            override fun onScrubStart(timeBar: TimeBar, position: Long) {
            }
            override fun onScrubStop(timeBar: TimeBar, position: Long, canceled: Boolean) {
                viewModel.player.seekTo(position)
                binding.timeBar.setPosition(position);
                binding.currentPositionTxt.text = Formater.formatDuration(position);
            }
        })
        //set timebar value keep updated
        binding.playerControlView.setProgressUpdateListener { position, bufferedPosition ->
            if(viewModel.player.isPlaying){
                binding.timeBar.setPosition(position)
                binding.timeBar.setBufferedPosition(bufferedPosition)
                binding.currentPositionTxt.text = Formater.formatDuration(position);
            }
        }
    }
}