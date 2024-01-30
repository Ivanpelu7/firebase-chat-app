package com.example.mychatfirebase

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mychatfirebase.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val db = Firebase.firestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
    }

    private fun initUI() {
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        binding.rvChats.layoutManager = LinearLayoutManager(this)
        val listaChats = arrayListOf<Chat>()

        FirebaseUtil.getChatsRef()
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val chat: Chat = document.toObject(Chat::class.java)

                    Log.d("chats", "${chat.nombreMiembro1} => ${chat.nombreMiembro2}")

                    listaChats.add(chat)

                    binding.rvChats.adapter = ChatsAdapter(listaChats)
                }
            }
    }
}