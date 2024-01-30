package com.example.mychatfirebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mychatfirebase.databinding.ActivityChatRoomBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ChatRoomActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatRoomBinding
    private lateinit var userId: String
    private lateinit var userName: String
    private lateinit var myName: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
    }

    private fun initUI() {
        userName = intent.getStringExtra("nombre").toString()
        userId = intent.getStringExtra("id").toString()
        myName = intent.getStringExtra("myName").toString()
        binding.tvNombreUsuario.text = userName
        initListeners()
        initChat()
    }

    private fun initChat() {
        val listaUsersID = arrayListOf<String>(FirebaseUtil.getCurrentUserID(), userId)
        val chat = Chat(listaUsersID, myName, userName)

        FirebaseUtil.getChatsRef()
            .add(chat)
            .addOnSuccessListener {

            }
    }

    private fun initListeners() {
        binding.ivBackArrow.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}