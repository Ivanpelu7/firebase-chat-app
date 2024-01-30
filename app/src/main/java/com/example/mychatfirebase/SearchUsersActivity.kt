package com.example.mychatfirebase

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mychatfirebase.databinding.ActivitySearchUsersBinding

class SearchUsersActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchUsersBinding
    private lateinit var myName: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
    }

    private fun initUI() {
        initListeners()
        mostrarUsuarios()
        myName = intent.getStringExtra("name").toString()
    }

    private fun initListeners() {
        binding.ivBackArrow.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

    }

    private fun mostrarUsuarios() {
        val listaUsuarios = arrayListOf<Usuario>()
        binding.rvUsers.layoutManager = LinearLayoutManager(this)

        FirebaseUtil.getUsersRef()
            .get()
            .addOnSuccessListener {
                for (usuario in it.documents) {
                    val idUsuario = usuario.getString("idUsuario")
                    val nombre = usuario.getString("nombre")
                    val email = usuario.getString("email")

                    val usuario = Usuario(idUsuario, nombre, email!!)

                    listaUsuarios.add(usuario)

                    binding.rvUsers.adapter = UsersAdapter(listaUsuarios, myName)
                }
            }
    }
}