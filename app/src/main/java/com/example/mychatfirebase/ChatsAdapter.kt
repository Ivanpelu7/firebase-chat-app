package com.example.mychatfirebase

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChatsAdapter(val chatsList: MutableList<Chat>) : RecyclerView.Adapter<ChatViewHolder>() {
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

    val tvNombre: TextView = view.findViewById(R.id.tvUser)

    fun render(chat: Chat) {
        FirebaseUtil.getCurrentUserDocumentRef()
            .get()
            .addOnSuccessListener {
                val name = it.getString("nombre")

                Log.d("name", "${name}")

                if (name.equals(chat.nombreMiembro1)) {
                    tvNombre.text = chat.nombreMiembro2
                } else {
                    tvNombre.text = chat.nombreMiembro1
                }
            }
    }
}