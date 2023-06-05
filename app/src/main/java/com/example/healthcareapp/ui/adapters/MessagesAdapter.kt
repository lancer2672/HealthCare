package com.example.healthcareapp.ui.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.healthcareapp.R
import com.example.healthcareapp.data.models.MessageModel
import com.example.healthcareapp.viewmodels.ChatViewModel
import de.hdodenhof.circleimageview.CircleImageView
import java.util.zip.Inflater

const val ITEM_SEND = 1
const val ITEM_RECEIVE = 2
class MessagesAdapter(private val context:
                      Context,private val viewModel: ChatViewModel):
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private val list = viewModel.messageList;
    inner class SenderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var circleImageView: CircleImageView;
        var msgtxt: TextView;

        init {
            circleImageView = itemView.findViewById(R.id.chat_user_avt);
            msgtxt = itemView.findViewById(R.id.chat_sender_msg);

        }
        fun bind(message:MessageModel){
            msgtxt.text = message.message;
        }
    }
    inner class ReceiverViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var circleImageView: CircleImageView;
        var msgtxt: TextView;

        init {
            circleImageView = itemView.findViewById(R.id.bot_avt);
            msgtxt = itemView.findViewById(R.id.chat_receiver_msg);

        }
        fun bind(message:MessageModel){
            msgtxt.text = message.message;
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ITEM_SEND) {
            val view = LayoutInflater.from(context).inflate(R.layout.sender_layout,parent,false);
            SenderViewHolder(view);
        } else {
            val view = LayoutInflater.from(context).inflate(R.layout.receiver_layout,parent,false);
            ReceiverViewHolder(view);
        }
    }

    override fun getItemCount(): Int {
      return  list.size;
    }

    override fun getItemViewType(position: Int): Int {
        return if(list[position].isBotMessage){
            ITEM_RECEIVE
        } else ITEM_SEND
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val msg = list[position];
        if(holder::class.java == SenderViewHolder::class.java ){
            val senderHolder =  holder as SenderViewHolder;
            senderHolder.bind(msg);
        }
        else{
            val receiverHolder =  holder as ReceiverViewHolder;
            receiverHolder.bind(msg);
        }
    }
}