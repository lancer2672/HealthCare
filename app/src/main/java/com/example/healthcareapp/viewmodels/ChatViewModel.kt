package com.example.healthcareapp.viewmodels

import android.database.Observable
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.chat.ChatCompletion
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.example.healthcareapp.data.models.MessageModel
import com.example.healthcareapp.data.repositories.ChatRepository
import com.example.healthcareapp.ui.fragments.Chat
import com.example.healthcareapp.ui.listeners.MessageListener
import com.example.healthcareapp.utils.Formater
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date

const val CHAT_GPT_API_KEY = "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"

class ChatViewModel: ViewModel() {
    private var messageListener:MessageListener? = null;

    var messageList: ArrayList<MessageModel> = arrayListOf();
    var message = ObservableField("");
    var isLoading = MutableLiveData(false);
    fun loadMesssages(){
        isLoading.value = true;
        ChatRepository.instance?.loadMessages(FirebaseAuth.getInstance().currentUser?.uid.toString()){messages->
            this.messageList.addAll(messages)
            this.messageListener?.messageIsSended();
            isLoading.value = false;
        }
    }
    @OptIn(BetaOpenAI::class)
    fun sendMessage(){
        val sendMessage = MessageModel(message.get().toString(),false, Formater.formatChatTime(Date()));
        message.set("");
        val receiveMessage = MessageModel("",true,Formater.formatChatTime(Date()));
        messageList.add(sendMessage)
        isLoading.value = true;
        viewModelScope.launch {
            val openAI = OpenAI(CHAT_GPT_API_KEY)
            try {
                val chatCompletionRequest = ChatCompletionRequest(
                    model = ModelId("gpt-3.5-turbo"),
                    messages = listOf(
                        ChatMessage(
                            role = ChatRole.User,
                            content = message.get().toString(),
                        )
                    )
                )
                val completion: ChatCompletion = openAI.chatCompletion(chatCompletionRequest)
                val response = completion.choices.first().message?.content
                receiveMessage.message = response?:"";
                Log.d("CHAT","Message send successfully")
                ChatRepository.instance?.saveMessage(sendMessage,
                    FirebaseAuth.getInstance().currentUser?.uid.toString()){
                    Log.d("CHAT","Message saved successfully")
                }
                ChatRepository.instance?.saveMessage(receiveMessage,
                    FirebaseAuth.getInstance().currentUser?.uid.toString()){
                    Log.d("CHAT","Message saved successfully")
                }

            } catch (e: Exception) {
                Log.e("CHAT",e.message?:"")
                receiveMessage.message = e.message?:"";
            }
            finally {
                messageList.add(receiveMessage);

                messageListener?.messageIsSended();
                isLoading.value = false;
            }
        }

    }
    fun setMessageListener(listener:MessageListener){
        this.messageListener = listener
    }

}