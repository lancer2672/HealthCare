package com.example.healthcareapp.ui.fragments

import android.content.res.ColorStateList
import android.os.Bundle
import android.transition.TransitionInflater
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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
import com.example.healthcareapp.ui.adapters.MessagesAdapter
import com.example.healthcareapp.ui.listeners.MessageListener
import com.example.healthcareapp.viewmodels.ChatViewModel
import kotlinx.coroutines.*

class Chat : Fragment(), MessageListener {
    private lateinit var binding: FragmentChatBinding;
    private lateinit var viewModel: ChatViewModel;
    private lateinit var messagesAdapter: MessagesAdapter;
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat, container, false)
        viewModel = ViewModelProvider(this)[ChatViewModel::class.java]
        messagesAdapter = MessagesAdapter(requireActivity(),viewModel);

        setUpViewModel();
        setEditTextTranformation();

        binding.recMessageList.adapter = messagesAdapter;
        binding.viewmodel = viewModel;

        return binding.root
    }
    private fun setEditTextTranformation(){
        binding.edtInput.setOnFocusChangeListener { _, hasFocus ->
            val transition = TransitionInflater.from(requireContext())
                .inflateTransition(R.transition.transition_comment_bar)
            TransitionManager.beginDelayedTransition(binding.root as ViewGroup, transition)
            if (hasFocus) {
                binding.userCommentAvt.visibility = View.GONE
                val params = binding.edtInput.layoutParams as LinearLayout.LayoutParams
                params.width = LinearLayout.LayoutParams.MATCH_PARENT - 40;
                binding.edtInput.layoutParams = params
            } else {
                binding.userCommentAvt.visibility = View.VISIBLE
                val params = binding.edtInput.layoutParams as LinearLayout.LayoutParams
                params.width = resources.getDimensionPixelSize(R.dimen.comment_input_width)
                binding.edtInput.layoutParams = params
            }
        }

    }
    private fun setUpViewModel(){
        viewModel.setMessageListener(this);
        viewModel.loadMesssages();
        viewModel.isLoading.observe(viewLifecycleOwner){isLoading ->
            if(isLoading){
                binding.recMessageList.visibility = View.GONE;
                binding.chatProgressBar.visibility = View.VISIBLE;
            }
            else{
                binding.recMessageList.visibility = View.VISIBLE;
                binding.chatProgressBar.visibility = View.GONE;
            }
        }
    }
    override fun messageIsSended() {
        messagesAdapter.notifyDataSetChanged();
    }
}