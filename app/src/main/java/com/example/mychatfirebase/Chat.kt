package com.example.mychatfirebase

data class Chat(
    val idChat: String,
    val usuarioPropietario: Usuario,
    val usuarioDestinatario: Usuario
)
