package com.example.mychatfirebase.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mychatfirebase.util.FirebaseUtil
import com.example.mychatfirebase.adapter.UsersAdapter
import com.example.mychatfirebase.model.Usuario
import com.example.mychatfirebase.databinding.ActivitySearchUsersBinding

class SearchUsersActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchUsersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
    }

    private fun initUI() {
        initListeners()
        mostrarUsuarios()
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
            .whereNotEqualTo("idUsuario", FirebaseUtil.getCurrentUserID())
            .get()
            .addOnSuccessListener { usuarios ->
                for (usuario in usuarios.documents) {
                    val idUsuario = usuario.getString("idUsuario")
                    val nombre = usuario.getString("nombre")
                    val email = usuario.getString("email")

                    val usuario = Usuario(idUsuario, nombre, email!!)

                    listaUsuarios.add(usuario)

                    binding.rvUsers.adapter = UsersAdapter(listaUsuarios)
                }
            }
    }
}