package com.example.healthcareapp.viewmodels

import android.content.Context
import android.os.CountDownTimer
import android.provider.MediaStore.Audio.Media
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthcareapp.data.models.MeditationHistory
import com.example.healthcareapp.data.models.MeditationModel
import com.example.healthcareapp.data.repositories.MeditationRepository
import com.example.healthcareapp.data.repositories.UserRepository
import com.example.healthcareapp.ui.activities.Meditation
import com.example.healthcareapp.ui.listeners.MeditationListener
import com.example.healthcareapp.ui.listeners.ShowBottomDialog
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MeditationViewModel(): ViewModel() {
    lateinit var player: ExoPlayer;
    var currentItem = MutableLiveData<MeditationModel?>(null);
    var listAudios = mutableListOf<MeditationModel>()
    var meditationListenr:MeditationListener? = null
    var showBottomDialog : ShowBottomDialog? = null
    var previousPosition: Long = 0
    var currentPosition: Long = 0
    private var timer: CountDownTimer? = null
    var currentTimerValue =  ObservableField<String>("")
    fun startMeditationSession(meditation: MeditationModel) {
        currentItem.value = meditation
        val mediaItem = MediaItem.fromUri(meditation.uri);
        saveMeditationHistory();
        player.setMediaItem(mediaItem);
        player.prepare();
        player.play();
    }
    fun getAudios() {
        runBlocking  {
            MeditationRepository.instance?.getMeditationAudios { meditationAudios ->
                listAudios.clear()
                listAudios.addAll(meditationAudios)
                Log.d("Audio", meditationAudios.size.toString())
            }
        }
    }
    fun saveMeditationHistory(){
        if(currentItem.value!=null){
            val historyItem = MeditationHistory()
                historyItem.fromMeditationModel(currentItem.value!!)
            Log.d("Meditation History", "Saving");
//                historyItem.duration = previousPosition
            AuthViewModel.getUser()
                ?.let { UserRepository.instance?.updateMeditationHistory(historyItem, it.userId) }
        }
    }
    override fun onCleared() {
        player.release()
        super.onCleared()
    }

    fun setTimer(time: String) {
        // Hủy bỏ timer cũ (nếu có)
        timer?.cancel()

        // Chuyển đổi giá trị time sang miliseconds
        val timeInMillis = time.toLongOrNull()?.times(60000) ?: 0
        Log.d("Timer","$timeInMillis")
        // Tạo timer mới với giá trị mới
        timer = object : CountDownTimer(timeInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = (millisUntilFinished / 1000) / 60
                val seconds = (millisUntilFinished / 1000) % 60
                currentTimerValue.set(String.format("%02d:%02d", minutes, seconds))

            }
            override fun onFinish() {
                // Dừng Player khi hết thời gian
                Log.d("Timer","Finish")
                currentTimerValue.set("")
                if(player.isPlaying)
                    player.pause()
            }
        }
        timer?.start()
    }
    fun getStartSession(): MeditationModel? {
        return listAudios.find { meditationModel -> meditationModel.type == "start" }
    }

    fun initPlayer(context: Context) {
        player = ExoPlayer.Builder(context).build()
//        override fun onCleared() {
//            player.release()
//            super.onCleared()
//        }
        player.addListener(object: Player.Listener {
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                super.onMediaItemTransition(mediaItem, reason)
                try{

                }
                catch (e:Exception){
                    Log.e("Exception", "onMediaItemTransition er ${e.message}")
                }
            }
        })

    }

}