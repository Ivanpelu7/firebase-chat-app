package com.example.mychatfirebase.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mychatfirebase.model.Usuario
import com.example.mychatfirebase.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
    }

    private fun initUI() {
        auth = Firebase.auth
        db = Firebase.firestore
        initListener()
    }

    private fun initListener() {
        binding.btRegistrarse.setOnClickListener {
            if (binding.etEmail.text.toString() != "" && binding.etPassword.text.toString() != "") {
                val email = binding.etEmail.text.toString()
                val password = binding.etPassword.text.toString()
                val name = binding.etNombre.text.toString()
                createAccount(email, password, name)
            }
        }

        binding.tvYaTengoCuenta.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun createAccount(email: String, password: String, name: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val usuario = Usuario(auth.uid!!, name, email)
                    guardarUsuario(usuario)
                    val intent = Intent(this, RecentChatsActivity::class.java)
                    intent.putExtra("nombre", usuario.nombre)
                    startActivity(intent)
                    finish()

                } else {
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }

    private fun guardarUsuario(user: Usuario) {
        Firebase.firestore.collection("users").document(user.idUsuario!!)
            .set(user)
            .addOnSuccessListener {
                Toast.makeText(
                    baseContext,
                    "Usuario creado con éxito.",
                    Toast.LENGTH_SHORT,
                ).show()
            }
    }
}