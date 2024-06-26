package com.example.mychatfirebase.data.model

import com.google.firebase.Timestamp

data class Chat(
    val chatId: String = "",
    val usersId: List<String>? = null,
    var lastMessageTimestamp: Timestamp? = Timestamp.now(),
    var lastMessageSenderId: String = "",
    var lastMessage: String = "",
    var unreadMessages: Int = 0
)

