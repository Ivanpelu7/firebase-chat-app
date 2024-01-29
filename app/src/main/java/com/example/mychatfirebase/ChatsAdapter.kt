package com.example.mychatfirebase

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChatsAdapter(val chatsList: List<Chat>) : RecyclerView.Adapter<ChatViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ChatViewHolder(layoutInflater.inflate(R.layout.chat_item, parent, false));
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val item = chatsList[position]
        holder.render(item)
    }

    override fun getItemCount(): Int = chatsList.size


}

class ChatViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val tvNombre = view.findViewById<TextView>(R.id.tvNombre)

    fun render(chat: Chat) {
        tvNombre.text = chat.usuarioDestinatario.nombre
    }
}