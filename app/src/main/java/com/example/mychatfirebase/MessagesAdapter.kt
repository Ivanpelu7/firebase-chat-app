package com.example.mychatfirebase

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class MessagesAdapter(options: FirestoreRecyclerOptions<Message>, val context: Context) : FirestoreRecyclerAdapter<Message, MessagesAdapter.MessageViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return MessageViewHolder(
            LayoutInflater.from(context).inflate(R.layout.message_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int, model: Message) {
        holder.render(model)
    }

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val receivedMessage: TextView = itemView.findViewById(R.id.messageReceived)
        val sendedMessage: TextView = itemView.findViewById(R.id.messageSended)

        fun render(message: Message) {
            if (message.idSender == FirebaseUtil.getCurrentUserID()) {
                receivedMessage.visibility = View.GONE
                sendedMessage.visibility = View.VISIBLE
                sendedMessage.text = message.message

            } else {
                receivedMessage.visibility = View.VISIBLE
                sendedMessage.visibility = View.GONE
                receivedMessage.text = message.message
            }
        }


    }
}