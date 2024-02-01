package com.example.mychatfirebase.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mychatfirebase.util.FirebaseUtil
import com.example.mychatfirebase.R
import com.example.mychatfirebase.model.Message
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class MessagesAdapter(options: FirestoreRecyclerOptions<Message>) : FirestoreRecyclerAdapter<Message, MessagesAdapter.MessageViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return MessageViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.message_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int, model: Message) {
        holder.render(model)
    }

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val receivedMessage: LinearLayout = itemView.findViewById(R.id.messageReceived)
        private val sendedMessage: LinearLayout = itemView.findViewById(R.id.messageSended)
        private val tvMessageSended: TextView = itemView.findViewById(R.id.tvMessageSended)
        private val tvReceivedMessage: TextView = itemView.findViewById(
            R.id.tvMessageReceived
        )
        fun render(message: Message) {
            if (message.idSender == FirebaseUtil.getCurrentUserID()) {
                receivedMessage.visibility = View.GONE
                sendedMessage.visibility = View.VISIBLE
                tvMessageSended.text = message.message

            } else {
                receivedMessage.visibility = View.VISIBLE
                sendedMessage.visibility = View.GONE
                tvReceivedMessage.text = message.message
            }
        }


    }
}