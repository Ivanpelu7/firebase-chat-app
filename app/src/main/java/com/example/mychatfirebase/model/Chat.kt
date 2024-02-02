package com.example.mychatfirebase.model

import com.google.firebase.Timestamp

data class Chat(
    val idChat: String = "",
    val usersId: List<String>? = null,
    var lastMessageTimestamp: Timestamp? = Timestamp.now(),
    var lastMessageSenderId: String = "",
    var lastMessage: String = "",
    var mensajesSinLeer: Int = 0
)

