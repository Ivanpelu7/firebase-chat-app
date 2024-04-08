package com.example.mychatfirebase.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mychatfirebase.data.model.Chat
import com.example.mychatfirebase.data.repository.ChatRepository
import kotlinx.coroutines.launch

class RecentChatsViewModel : ViewModel() {

    private val _recentChats = MutableLiveData<List<Chat>>()
    val recentChats: LiveData<List<Chat>> get() = _recentChats

    private var _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val chatRepository = ChatRepository()

    init {
        getRecentChats()
    }


    fun getRecentChats() {
        viewModelScope.launch {
            chatRepository.getMyChats() { chats ->
                _recentChats.value = chats
            }
            _isLoading.value = false
        }
    }
}