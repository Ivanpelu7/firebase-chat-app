package com.example.mychatfirebase.data.repository

import com.example.mychatfirebase.data.model.Chat
import com.example.mychatfirebase.data.model.Message
import com.example.mychatfirebase.util.FirebaseUtil
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class MessageRepository {

    suspend fun getChatMessages(chatId: String, onMessageSend: (List<Message>) -> Unit) {
        withContext(Dispatchers.IO) {
            FirebaseUtil.getMessagesRef(chatId)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener { value, _ ->
                    val messages = mutableListOf<Message>()

                    for (document in value!!.documents) {
                        messages.add(document.toObject(Message::class.java)!!)
                    }

                    onMessageSend(messages)
                }
        }
    }

    suspend fun addMessage(chatId: String, message: Message) {
        withContext(Dispatchers.IO) {
            FirebaseUtil.getMessagesRef(chatId).add(message).await()
        }
    }
}