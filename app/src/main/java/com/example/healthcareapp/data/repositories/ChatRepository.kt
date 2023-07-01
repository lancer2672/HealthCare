package com.example.healthcareapp.data.repositories

import android.util.Log
import com.example.healthcareapp.data.models.ChatModel
import com.example.healthcareapp.data.models.MessageModel
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

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
    fun loadMessages(userId: String, onSuccess: (List<MessageModel>) -> Unit, onFailure: (() -> Unit)? = null) {
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
                    if (onFailure != null) {
                        onFailure()
                    }
                    Log.d("CHAT", "Chat document with userId $userId not found")
                }
            }
            .addOnFailureListener { e ->
                Log.w("CHAT", "Error retrieving chat document", e)
            }
    }
    fun create(userId: String, onSuccess: (() -> Unit)? = null, onFailure: (() -> Unit)? = null) {
        val chat = ChatModel(userId)
        chatRef.add(chat)
            .addOnSuccessListener {
                if (onSuccess != null) {
                    onSuccess()
                }
                Log.w("CHAT", "Create successfully")
            }
            .addOnFailureListener { e ->
                if (onFailure != null) {
                    onFailure()
                }
                Log.w("CHAT", "Error adding chat document", e)
            }
    }

}

