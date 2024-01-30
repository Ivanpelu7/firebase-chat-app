package com.example.mychatfirebase

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.mychatfirebase.databinding.ActivityChatRoomBinding
import com.google.firebase.Timestamp

class ChatRoomActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatRoomBinding
    private lateinit var userId: String
    private lateinit var userName: String
    private lateinit var myName: String
    private lateinit var idChat: String
    private lateinit var chat: Chat


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
        idChat = intent.getStringExtra("idChat").toString()
        binding.tvNombreUsuario.text = userName
        initListeners()
        initChat()
    }

    private fun initChat() {
        val listaUsers = arrayListOf(FirebaseUtil.getCurrentUserID(), userId)

        // Comprobar si ya existe ese chat
        FirebaseUtil.getChatsRef()
            .whereEqualTo("usersId", listaUsers)
            .get()
            .addOnSuccessListener {
                if (it == null || it.documents.isEmpty()) {
                    val idAuto = FirebaseUtil.getChatsRef().document().id

                    chat = Chat(idAuto, listaUsers, myName, userName)

                    FirebaseUtil.getChatsRef().document(idAuto).set(chat)
                } else {

                }
            }
    }

    private fun initRecyclerView() {

    }

    private fun initListeners() {
        binding.ivBackArrow.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.ivSendMessage.setOnClickListener {
            val message = binding.etMessage.text.toString()
            if (message.isNotEmpty()) {
                sendMessage(message)
            }
        }
    }

    private fun sendMessage(message: String) {
        val chatMap = mapOf<String, Any>(
            "lastMessageSenderId" to FirebaseUtil.getCurrentUserID(),
            "lastMessageTimestamp" to Timestamp.now()
        )

        if (idChat != null) {
            FirebaseUtil.getChatsRef().document(idChat).update(chatMap)
            val message = Message(message, FirebaseUtil.getCurrentUserID(), Timestamp.now())
            FirebaseUtil.getMessagesRef(idChat).add(message).addOnSuccessListener {
            }
        } else {
            FirebaseUtil.getChatsRef().document(chat.idChat).update(chatMap)
            val message = Message(message, FirebaseUtil.getCurrentUserID(), Timestamp.now())
            FirebaseUtil.getMessagesRef(chat.idChat).add(message).addOnSuccessListener {
            }
        }




    }
}



