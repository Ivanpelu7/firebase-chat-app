package com.example.mychatfirebase

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mychatfirebase.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var nombre: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
    }

    private fun initUI() {
        setUpRecyclerView()
        initListeners()
        nombre = intent.getStringExtra("nombre").toString()
    }

    private fun initListeners() {
        binding.ivSearch.setOnClickListener {
            val intent = Intent(this, SearchUsersActivity::class.java)
            intent.putExtra("name", nombre)
            startActivity(intent)
        }

        binding.ivExit.setOnClickListener {
            Firebase.auth.signOut()
            val intent = Intent(this, StartActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setUpRecyclerView() {
        binding.rvChats.layoutManager = LinearLayoutManager(this)
        val listaChats = arrayListOf<Chat>()

        FirebaseUtil.getChatsRef()
            .whereArrayContains("usersId", FirebaseUtil.getCurrentUserID())
            .addSnapshotListener { value, error ->
                if (value != null) {
                    for (document in value) {
                        val chat: Chat = document.toObject(Chat::class.java)

                        Log.d("chats", "${chat.nombreMiembro1} => ${chat.nombreMiembro2}")

                        listaChats.add(chat)

                        binding.rvChats.adapter = ChatsAdapter(listaChats, nombre)
                    }
                }
            }

    }
}