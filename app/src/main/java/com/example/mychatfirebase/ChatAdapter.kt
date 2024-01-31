package com.example.mychatfirebase

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class ChatAdapter(options: FirestoreRecyclerOptions<Chat>, val nombre: String) :
    FirestoreRecyclerAdapter<Chat, ChatAdapter.ChatViewHolder>(options) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatAdapter.ChatViewHolder {
        return ChatViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.chat_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ChatAdapter.ChatViewHolder, position: Int, model: Chat) {
        holder.render(model, nombre)
    }

    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvNombre: TextView = itemView.findViewById(R.id.tvUser)
        val itemLayout: CardView = itemView.findViewById(R.id.itemLayout)
        val context = itemView.context
        val tvTimestamp: TextView = itemView.findViewById(R.id.tvTimestamp)
        val lastMessage: TextView = itemView.findViewById(R.id.tvLastMessage)

        fun render(chat: Chat, nombre: String) {
            FirebaseUtil.getOtherUserFromChat(chat.usersId!!)
                .get()
                .addOnSuccessListener {
                    val usuario = it.toObject(Usuario::class.java)

                    tvNombre.text = usuario!!.nombre

                    if (chat.lastMessage.isNotEmpty() && chat.lastMessageTimestamp != null) {
                        tvTimestamp.text =
                            FirebaseUtil.timestampToString(chat.lastMessageTimestamp!!)

                        if (chat.lastMessageSenderId == FirebaseUtil.getCurrentUserID()) {
                            lastMessage.text = "Tu: ${chat.lastMessage}"

                        } else {
                            lastMessage.text = chat.lastMessage
                        }

                    } else {
                        lastMessage.text = "No hay mensajes todavía"
                    }

                    itemLayout.setOnClickListener {
                        val intent = Intent(context, ChatRoomActivity::class.java)
                        intent.putExtra("idChat", chat.idChat)
                        intent.putExtra("nombre", usuario.nombre)
                        intent.putExtra("userId", usuario.idUsuario)
                        intent.putExtra("myName", nombre)

                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        context.startActivity(intent)

                    }
                }
        }
    }
}