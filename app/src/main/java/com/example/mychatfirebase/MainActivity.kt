package com.example.mychatfirebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mychatfirebase.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var usuariosList: ArrayList<Usuario>
    private lateinit var chatsList: ArrayList<Chat>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        usuariosList = getUsuarios()
        chatsList = getChats()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding.rvChats.layoutManager = LinearLayoutManager(this)
        binding.rvChats.adapter = ChatsAdapter(chatsList)
    }

    private fun getChats(): ArrayList<Chat> {
        val db = Firebase.firestore

        val chatsList = ArrayList<Chat>()

        db.collection("chats")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    // Log.d(TAG, "${document.id} => ${document.data}")
                    val idPropietario = document.data.get("idPropietario").toString()
                    val idChat = document.data.get("idChat").toString()
                    val idDestinatario = document.data.get("idDestinatario").toString()

                    val usuarioDestinatario: Usuario = getUser(idDestinatario)!!
                    val usuarioPropietario: Usuario = getUser(idPropietario)!!

                    val chat = Chat(idChat, usuarioPropietario, usuarioDestinatario)
                    chatsList.add(chat)
                }
            }
            .addOnFailureListener { exception ->
                Log.w("chats", "Error getting documents: ", exception)
            }

        return chatsList
    }

    private fun getUsuarios(): ArrayList<Usuario> {
        val db = Firebase.firestore

        val usuariosList = ArrayList<Usuario>()

        db.collection("usuarios")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    // Log.d(TAG, "${document.id} => ${document.data}")
                    val idUsuario = document.getString("idUsuario")
                    val nombre = document.data.get("nombre").toString()
                    val password = document.data.get("password").toString()

                    val usuario = Usuario(idUsuario, nombre, password)
                    usuariosList.add(usuario)
                }
            }
            .addOnFailureListener { exception ->
                Log.w("usuarios", "Error getting documents: ", exception)
            }

        return usuariosList
    }

    private fun getUser(id: String): Usuario? {
        for (user in usuariosList) {
            if (user.idUsuario.equals(id)) {
                return user
            }
        }

        return null
    }
}