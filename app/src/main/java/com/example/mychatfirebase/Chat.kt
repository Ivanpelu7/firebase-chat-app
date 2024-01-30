package com.example.mychatfirebase

import com.google.firebase.Timestamp
data class Chat(
    val idChat: String = "",
    val usersId: ArrayList<String>? = null,
    val nombreMiembro1: String = "",
    val nombreMiembro2: String = "",
    var lastMessageTimestamp: Timestamp? = null,
    var lastMessageSenderId: String = "",
)
