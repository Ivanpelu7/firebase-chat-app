package com.example.mychatfirebase

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mychatfirebase.databinding.ActivityMainBinding
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var nombre: String
    private lateinit var adapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
    }

    private fun initUI() {
        nombre = intent.getStringExtra("nombre").toString()
        setUpRecyclerView()
        initListeners()

    }

    private fun initListeners() {
        binding.ivSearch.setOnClickListener {
            val intent = Intent(this, SearchUsersActivity::class.java)
            intent.putExtra("name", nombre)
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

        adapter = ChatAdapter(options)
        binding.rvChats.layoutManager = LinearLayoutManager(this)
        binding.rvChats.adapter = adapter
        adapter.startListening()
    }

}