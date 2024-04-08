package com.example.mychatfirebase.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mychatfirebase.adapter.ChatAdapter
import com.example.mychatfirebase.databinding.ActivityMainBinding
import com.example.mychatfirebase.viewmodel.RecentChatsViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RecentChatsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val recentChatsViewModel: RecentChatsViewModel by viewModels()
    private lateinit var recyclerAdapter: ChatAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
    }

    private fun initUI() {
        setUpRecyclerView()
        initListeners()
        initObservers()
    }

    private fun initObservers() {
        recentChatsViewModel.recentChats.observe(this) { recentChats ->
            recyclerAdapter.updateList(recentChats)
        }

        recentChatsViewModel.isLoading.observe(this) { isLoading ->
            if (!isLoading) binding.progressBar.visibility = View.GONE
        }
    }

    private fun initListeners() {
        binding.ivSearch.setOnClickListener {
            val intent = Intent(this, UsersListActivity::class.java)
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
        recyclerAdapter = ChatAdapter()
        binding.rvChats.layoutManager = LinearLayoutManager(this)
        binding.rvChats.adapter = recyclerAdapter
    }

}