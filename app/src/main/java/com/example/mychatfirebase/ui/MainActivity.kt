package com.example.mychatfirebase.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mychatfirebase.model.Chat
import com.example.mychatfirebase.adapter.ChatAdapter
import com.example.mychatfirebase.util.FirebaseUtil
import com.example.mychatfirebase.databinding.ActivityMainBinding
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
    }

    private fun initUI() {
        initListeners()
        setUpRecyclerView()
    }

    private fun initListeners() {
        binding.ivSearch.setOnClickListener {
            val intent = Intent(this, SearchUsersActivity::class.java)
            startActivity(intent)
        }

        binding.ivExit.setOnClickListener {
            Firebase.auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun setUpRecyclerView() {
        val query: Query = FirebaseUtil.getChatsRef()
            .whereArrayContains("usersId", FirebaseUtil.getCurrentUserID())
            .orderBy("lastMessageTimestamp", Query.Direction.DESCENDING)


        val options: FirestoreRecyclerOptions<Chat> = FirestoreRecyclerOptions.Builder<Chat>()
            .setQuery(query, Chat::class.java)
            .build()

        val chatsAdapter = ChatAdapter(options)
        binding.rvChats.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = chatsAdapter
        }

        chatsAdapter.startListening()
    }

}