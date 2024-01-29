package com.example.mychatfirebase

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mychatfirebase.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
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

        //initRecyclerView()
        auth = Firebase.auth

        binding.tvDatos.text = auth.currentUser!!.email

        binding.btCerrarSesion.setOnClickListener {
            cerrarSesion()
        }
    }

    private fun initRecyclerView() {
        val listaUsuarios = mutableListOf<Usuario>()
        val adapter = ChatsAdapter(listaUsuarios)

        db.collection("usuarios")
            .get()
            .addOnSuccessListener { resultado ->
                for (document in resultado) {
                    val usuario = document.toObject(Usuario::class.java)
                    binding.rvChats.adapter = adapter
                    binding.rvChats.layoutManager = LinearLayoutManager(this)
                    listaUsuarios.add(usuario)
                }

            }
            .addOnFailureListener { exception ->
                Log.w("usuarios", "Error getting documents: ", exception)
            }
    }

    private fun cerrarSesion() {
        Firebase.auth.signOut()

        val intent = Intent(this, StartActivity::class.java)
        startActivity(intent)
    }
}