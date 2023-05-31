package com.example.healthcareapp.ui.fragments

import android.os.Bundle
import android.transition.TransitionInflater
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.chat.ChatCompletion
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.example.healthcareapp.R
import com.example.healthcareapp.databinding.FragmentChatBinding
import kotlinx.coroutines.*

class Chat : Fragment() {

    private var gptResponse = ""
    private val gptQuery = ""
    private lateinit var binding: FragmentChatBinding;
    @OptIn(BetaOpenAI::class)
    fun getGPTResponse() {
        lifecycleScope.launch {
            val openAI = OpenAI(CHAT_GPT_API_KEY)
            try {
                val chatCompletionRequest = ChatCompletionRequest(
                    model = ModelId("gpt-3.5-turbo"),
                    messages = listOf(
                        ChatMessage(
                            role = ChatRole.User,
                            content = binding.editQuery.text.toString()
                        )
                    )
                )
                val completion: ChatCompletion = openAI.chatCompletion(chatCompletionRequest)
                val response = completion.choices.first().message?.content
                gptResponse = response ?: ""
                binding.tvAnswer.text = gptResponse;
            } catch (e: Exception) {
                gptResponse = "ERROR: ${e.message ?: ""}"
            }
        }
    }

    companion object {
        const val CHAT_GPT_API_KEY = "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" //R
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat, container, false)
        binding.btnChat.setOnClickListener {
            getGPTResponse();
        }
        setEditTextTranformation();

        // Inflate the layout for this fragment
        return binding.root
    }
    fun setEditTextTranformation(){
        binding.edtInput.doOnTextChanged { text, start, before, count ->
            if (text.isNullOrEmpty()) {
                binding.sendBtn.visibility = View.INVISIBLE;
            } else {
                binding.sendBtn.visibility = View.VISIBLE;
            }
    }
        binding.edtInput.setOnFocusChangeListener { _, hasFocus ->
            val transition = TransitionInflater.from(requireContext())
                .inflateTransition(R.transition.transition_comment_bar)
            TransitionManager.beginDelayedTransition(binding.root as ViewGroup, transition)
            if (hasFocus) {

                binding.userCommentAvt.visibility = View.GONE
                val params = binding.edtInput.layoutParams as LinearLayout.LayoutParams
                params.width = LinearLayout.LayoutParams.MATCH_PARENT
                binding.edtInput.layoutParams = params
            } else {


                binding.userCommentAvt.visibility = View.VISIBLE
                val params = binding.edtInput.layoutParams as LinearLayout.LayoutParams
                params.width = resources.getDimensionPixelSize(R.dimen.comment_input_width)
                binding.edtInput.layoutParams = params
            }
        }

    }
}