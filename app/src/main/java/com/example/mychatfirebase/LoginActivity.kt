package com.example.mychatfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.mychatfirebase.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlin.math.sign

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
    }

    private fun initUI() {
        auth = Firebase.auth
        initListener()
    }

    private fun initListener() {
        binding.btLogin.setOnClickListener {
            if (binding.etEmail.text.toString() != "" && binding.etPassword.text.toString() != "") {
                val email = binding.etEmail.text.toString()
                val password = binding.etPassword.text.toString()
                signIn(email, password)
            }
        }
    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    FirebaseUtil.getCurrentUserDocumentRef()
                        .get()
                        .addOnSuccessListener {
                            val nombre = it.getString("nombre")

                            Log.d("usuario", "${it.data}")

                            val intent = Intent(this, MainActivity::class.java)
                            intent.putExtra("nombre", nombre)
                            startActivity(intent)
                            finish()
                        }


                } else {
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }
}