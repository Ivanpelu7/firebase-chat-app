package com.example.mychatfirebase

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mychatfirebase.databinding.ActivityChatRoomBinding
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query

class ChatRoomActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatRoomBinding
    private lateinit var otherUserID: String
    private lateinit var otherUserName: String
    private lateinit var chatID: String
    private var isChatOpen: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
    }

    private fun initUI() {
        getIntents()
        binding.tvNombreUsuario.text = otherUserName
        setUpRecyclerView()
        initListeners()
    }

    private fun getIntents() {
        otherUserName = intent.getStringExtra("otherUserName").toString()
        otherUserID = intent.getStringExtra("otherUserID").toString()

        if (!intent.hasExtra("chatID")) {
            createNewChat()

        } else {
            chatID = intent.getStringExtra("chatID").toString()
        }
    }

    private fun initListeners() {
        binding.ivBackArrow.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.ivSendMessage.setOnClickListener {
            val message = binding.etMessage.text.toString()
            if (message.isNotEmpty()) {
                sendMessage(message)
                binding.etMessage.text = null
            }
        }
    }

    private fun createNewChat() {
        val chatUsers = arrayListOf(FirebaseUtil.getCurrentUserID(), otherUserID).sorted()
        chatID = FirebaseUtil.getChatsRef().document().id
        val newChat = Chat(chatID, chatUsers)
        FirebaseUtil.getChatsRef().document(chatID).set(newChat)
    }

    private fun setUpRecyclerView() {
        val query: Query =
            FirebaseUtil.getMessagesRef(chatID).orderBy("timestamp", Query.Direction.DESCENDING)

        val options: FirestoreRecyclerOptions<Message> = FirestoreRecyclerOptions.Builder<Message>()
            .setQuery(query, Message::class.java)
            .build()

        val messagesAdapter = MessagesAdapter(options)
        val manager = LinearLayoutManager(this).apply {
            reverseLayout = true
        }
        binding.rvMessages.apply {
            layoutManager = manager
            adapter = messagesAdapter
        }

        messagesAdapter.startListening()

        // Este código se ejecuta cada vez que aparece un nuevo mensaje en el chat
        messagesAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                // Scroll automático hasta el nuevo mensaje
                binding.rvMessages.scrollToPosition(0)

                // Comprobar si no es el actual usuario el que ha enviado el último mensaje
                // y si tiene el chat abierto para resetear los mensajes sin leer
                if (checkLastSenderID() && isChatOpen) restartUnreadMessages()
            }
        })
    }

    private fun checkLastSenderID(): Boolean {
        var flag = false

        FirebaseUtil.getChatsRef().document(chatID)
            .get()
            .addOnSuccessListener { message ->
                if (message.getString("lastMessageSenderId") != FirebaseUtil.getCurrentUserID()) {
                    flag = true
                }
            }

        return flag
    }

    private fun restartUnreadMessages() {
        FirebaseUtil.getChatsRef().document(chatID)
            .update("mensajesSinLeer", 0)
    }

    private fun sendMessage(message: String) {
        val chatMap = mapOf(
            "lastMessageSenderId" to FirebaseUtil.getCurrentUserID(),
            "lastMessageTimestamp" to Timestamp.now(),
            "lastMessage" to message,
            "mensajesSinLeer" to FieldValue.increment(1)
        )

        // Actualizamos los campos del chat con los datos del chatMap
        FirebaseUtil.getChatsRef().document(chatID).update(chatMap)

        // Añadimos el mensaje a la coleccion de mensajes del chat actual
        val newMessage = Message(message, FirebaseUtil.getCurrentUserID(), Timestamp.now())
        FirebaseUtil.getMessagesRef(chatID).add(newMessage)
    }

    override fun onStop() {
        super.onStop()
        isChatOpen = false
    }
}



