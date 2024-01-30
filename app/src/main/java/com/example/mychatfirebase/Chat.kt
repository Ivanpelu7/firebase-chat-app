package com.example.mychatfirebase

data class Chat(
    val usersId: ArrayList<String>? = null,
    val nombreMiembro1: String = "",
    val nombreMiembro2: String = "",
    val lastMessageSenderId: String = ""
)
