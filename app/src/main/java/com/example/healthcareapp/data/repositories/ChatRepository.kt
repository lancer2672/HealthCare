package com.example.healthcareapp.data.repositories

import android.util.Log
import com.example.healthcareapp.data.models.ChatModel
import com.example.healthcareapp.data.models.MessageModel
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import java.util.Objects

class ChatRepository {
    private val chatRef = FirebaseFirestore.getInstance().collection("chat")
    companion object{
        var instance: ChatRepository? = null
            get() {
                if (field == null) {
                    field = ChatRepository()
                }
                return field
            }
            private set
    }
    fun saveMessage(message: MessageModel, userId: String, onSuccess: ()->Unit){
        val chatRef = chatRef.whereEqualTo("userId", userId).limit(1);
        chatRef.get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val chatDoc = querySnapshot.documents[0]
                    chatDoc.reference.update("messages", FieldValue.arrayUnion(message))


                        .addOnSuccessListener {
                            onSuccess() // Call onSuccess when the operation is successful
                        }
                        .addOnFailureListener { e ->
                            Log.w("CHAT", "Error updating document", e)
                        }
                } else {
                    Log.d("CHAT", "Chat document with userId $userId not found")
                }
            }
            .addOnFailureListener { e ->
                Log.w("CHAT", "Error retrieving chat document", e)
            }
    }
    fun loadMessages(userId: String, onSuccess: (List<MessageModel>) -> Unit) {
        val chatRef = chatRef.whereEqualTo("userId", userId).limit(1)
        chatRef.get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    var messageList: List<MessageModel> = mutableListOf();
                    for (document in querySnapshot) {
                        val chatModel = document.toObject(ChatModel::class.java)
                        messageList = chatModel.messages;
                    }

                    onSuccess(messageList) // Call onSuccess with the loaded message list
                } else {
                    Log.d("CHAT", "Chat document with userId $userId not found")
                }
            }
            .addOnFailureListener { e ->
                Log.w("CHAT", "Error retrieving chat document", e)
            }
    }
}

