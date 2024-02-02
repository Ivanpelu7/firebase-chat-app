package com.example.mychatfirebase.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.mychatfirebase.util.FirebaseUtil
import com.example.mychatfirebase.R
import com.example.mychatfirebase.model.Chat
import com.example.mychatfirebase.model.Usuario
import com.example.mychatfirebase.ui.ChatRoomActivity
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class ChatAdapter(options: FirestoreRecyclerOptions<Chat>) :
    FirestoreRecyclerAdapter<Chat, ChatAdapter.ChatViewHolder>(options) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        return ChatViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.chat_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int, model: Chat) {
        holder.render(model)
    }


    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvNombre: TextView = itemView.findViewById(R.id.tvUser)
        private val itemLayout: CardView = itemView.findViewById(R.id.itemLayout)
        val context: Context = itemView.context
        private val tvTimestamp: TextView = itemView.findViewById(R.id.tvTimestamp)
        private val lastMessage: TextView = itemView.findViewById(R.id.tvLastMessage)
        private val tvNotificacion: TextView = itemView.findViewById(R.id.notificacion)

        fun render(chat: Chat) {
            FirebaseUtil.getOtherUserFromChat(chat.usersId!!)
                .get()
                .addOnSuccessListener {
                    val usuario = it.toObject(Usuario::class.java)!!

                    itemLayout.visibility = View.VISIBLE

                    tvNombre.text = usuario.nombre

                    if (chat.lastMessage.isNotEmpty() && chat.lastMessageTimestamp != null) {
                        tvTimestamp.text =
                            FirebaseUtil.timestampToString(chat.lastMessageTimestamp!!)

                        if (chat.lastMessage.length > 20) {
                            val shortString =
                                chat.lastMessage.substring(0, minOf(chat.lastMessage.length, 20))

                            lastMessage.text =
                                if (FirebaseUtil.getCurrentUserID() == chat.lastMessageSenderId) {
                                    "Tu: ${shortString}..."
                                } else {
                                    "${shortString}..."
                                }

                        } else {
                            lastMessage.text =
                                if (FirebaseUtil.getCurrentUserID() == chat.lastMessageSenderId) {
                                    "Tu: ${chat.lastMessage}"
                                } else {
                                    chat.lastMessage
                                }
                        }
                    }

                    if (chat.mensajesSinLeer > 0 && chat.lastMessageSenderId != FirebaseUtil.getCurrentUserID()) {
                        tvNotificacion.apply {
                            visibility = View.VISIBLE
                            text = chat.mensajesSinLeer.toString()
                        }

                    } else {
                        tvNotificacion.visibility = View.GONE
                    }

                    itemLayout.setOnClickListener {
                        if (chat.lastMessageSenderId != FirebaseUtil.getCurrentUserID()) {
                            FirebaseUtil.getChatsRef()
                                .document(chat.idChat)
                                .update("mensajesSinLeer", 0)
                        }

                        val intent = Intent(context, ChatRoomActivity::class.java)
                        intent.putExtra("chatID", chat.idChat)
                        intent.putExtra("otherUserName", usuario.nombre)
                        intent.putExtra("otherUserID", usuario.idUsuario)
                        context.startActivity(intent)
                    }
                }
        }
    }
}