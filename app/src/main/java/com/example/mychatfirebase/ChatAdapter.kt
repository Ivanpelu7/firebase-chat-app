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
        fun render(chat: Chat, nombre: String) {
            if (nombre == chat.nombreMiembro1) {
                tvNombre.text = chat.nombreMiembro2
            } else {
                tvNombre.text = chat.nombreMiembro1
            }

            itemLayout.setOnClickListener {

                var otherId = ""

                for (id in chat.usersId!!) {
                    if (id != FirebaseUtil.getCurrentUserID()) {
                        otherId = id
                    }
                }

                val intent = Intent(context, ChatRoomActivity::class.java)
                intent.putExtra("idChat", chat.idChat)
                intent.putExtra("nombre", tvNombre.text.toString())
                intent.putExtra("userId", otherId)

                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)

            }
        }


    }
}