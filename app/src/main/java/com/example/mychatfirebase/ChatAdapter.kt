package com.example.mychatfirebase

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class ChatAdapter(options: FirestoreRecyclerOptions<Chat>) :
    FirestoreRecyclerAdapter<Chat, ChatAdapter.ChatViewHolder>(options) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatAdapter.ChatViewHolder {
        return ChatViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.chat_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ChatAdapter.ChatViewHolder, position: Int, model: Chat) {
        holder.render(model)
    }

    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvNombre: TextView = itemView.findViewById(R.id.tvUser)
        val itemLayout: CardView = itemView.findViewById(R.id.itemLayout)
        val context = itemView.context
        val tvTimestamp: TextView = itemView.findViewById(R.id.tvTimestamp)
        val lastMessage: TextView = itemView.findViewById(R.id.tvLastMessage)
        val tvNotificacion: TextView = itemView.findViewById(R.id.notificacion)

        fun render(chat: Chat) {
            FirebaseUtil.getOtherUserFromChat(chat.usersId!!)
                .get()
                .addOnSuccessListener {
                    val usuario = it.toObject(Usuario::class.java)

                    tvNombre.text = usuario!!.nombre

                    if (chat.lastMessage.isNotEmpty() && chat.lastMessageTimestamp != null) {
                        tvTimestamp.text = FirebaseUtil.timestampToString(chat.lastMessageTimestamp!!)

                        val stringRecortado = chat.lastMessage.substring(0, minOf(chat.lastMessage.length, 20))

                        if (chat.lastMessage.length > 20) {
                            if (chat.lastMessageSenderId == FirebaseUtil.getCurrentUserID()) {
                                lastMessage.text = "Tu: ${stringRecortado}..."

                            } else {
                                lastMessage.text = "${stringRecortado}..."
                            }

                        } else {
                            if (chat.lastMessageSenderId == FirebaseUtil.getCurrentUserID()) {
                                lastMessage.text = "Tu: ${stringRecortado}"

                            } else {
                                lastMessage.text = "${stringRecortado}"
                            }
                        }



                    } else {
                        lastMessage.text = "No hay mensajes todavía"
                    }

                    if (chat.mensajesSinLeer > 0 && chat.lastMessageSenderId != FirebaseUtil.getCurrentUserID()) {
                        tvNotificacion.visibility = View.VISIBLE
                        tvNotificacion.text = chat.mensajesSinLeer.toString()
                    } else {
                        tvNotificacion.visibility = View.INVISIBLE
                    }

                    itemLayout.setOnClickListener {
                        if (chat.lastMessageSenderId != FirebaseUtil.getCurrentUserID()) {
                            FirebaseUtil.getChatsRef()
                                .document(chat.idChat)
                                .update("mensajesSinLeer", 0)
                        }


                        val intent = Intent(context, ChatRoomActivity::class.java)
                        intent.putExtra("idChat", chat.idChat)
                        intent.putExtra("nombre", usuario.nombre)
                        intent.putExtra("userId", usuario.idUsuario)

                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        context.startActivity(intent)

                    }
                }
        }
    }
}