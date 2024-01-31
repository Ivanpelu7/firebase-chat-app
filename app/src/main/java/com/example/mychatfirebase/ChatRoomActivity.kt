package com.example.mychatfirebase

import android.os.Bundle
import android.util.Log
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
    private lateinit var userId: String
    private lateinit var userName: String
    private lateinit var myName: String
    private lateinit var idChat: String
    private lateinit var chat: Chat
    private lateinit var adapter: MessagesAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
    }

    private fun initUI() {
        userName = intent.getStringExtra("nombre").toString()
        userId = intent.getStringExtra("userId").toString()
        myName = intent.getStringExtra("myName").toString()
        binding.tvNombreUsuario.text = userName
        initListeners()
        initChat()
    }

    private fun initChat() {
        val listaUsers = arrayListOf(FirebaseUtil.getCurrentUserID(), userId).sorted()

        // Comprobar si ya existe ese chat
        FirebaseUtil.getChatsRef()
            .whereEqualTo("usersId", listaUsers)
            .get()
            .addOnSuccessListener {
                if (it == null || it.documents.isEmpty()) {
                    idChat = FirebaseUtil.getChatsRef().document().id
                    chat = Chat(idChat, listaUsers)
                    FirebaseUtil.getChatsRef().document(idChat).set(chat)

                } else {
                    for (document in it.documents) {
                        idChat = document.getString("idChat").toString()
                        Log.d("idchat", idChat)
                    }
                }

                initRecyclerView()
            }
    }

    private fun initRecyclerView() {
        val query: Query =
            FirebaseUtil.getMessagesRef(idChat).orderBy("timestamp", Query.Direction.DESCENDING)

        val options: FirestoreRecyclerOptions<Message> = FirestoreRecyclerOptions.Builder<Message>()
            .setQuery(query, Message::class.java)
            .build()

        adapter = MessagesAdapter(options, applicationContext)
        val manager = LinearLayoutManager(this)
        manager.reverseLayout = true
        binding.rvMessages.layoutManager = manager
        binding.rvMessages.adapter = adapter
        adapter.startListening()

        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                binding.rvMessages.scrollToPosition(0)
            }
        })
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

    private fun sendMessage(message: String) {
        val chatMap = mapOf<String, Any>(
            "lastMessageSenderId" to FirebaseUtil.getCurrentUserID(),
            "lastMessageTimestamp" to Timestamp.now(),
            "lastMessage" to message
        )

        FirebaseUtil.getChatsRef().document(idChat).update(chatMap)
        val message = Message(message, FirebaseUtil.getCurrentUserID(), Timestamp.now())
        FirebaseUtil.getMessagesRef(idChat).add(message)
    }
}



