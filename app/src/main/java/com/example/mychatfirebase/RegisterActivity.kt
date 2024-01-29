package com.example.mychatfirebase

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mychatfirebase.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
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
        binding.btRegister.setOnClickListener {
            if (binding.etEmail.text.toString() != "" && binding.etPassword.text.toString() != "") {
                val email = binding.etEmail.text.toString()
                val password = binding.etPassword.text.toString()
                createAccount(email, password)
            }
        }
    }

    private fun createAccount(email: String, password: String) {
        // [START create_user_with_email]
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    guardarInformacionUsuario(user)
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)

                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
        // [END create_user_with_email]
    }

    fun guardarInformacionUsuario(user: FirebaseUser?) {
        user?.let { user ->
            // Crea un nuevo documento en la colección "usuarios" con el ID del usuario
            val usuarioDocRef = db.collection("usuarios").document(user.uid)

            // Puedes agregar más campos según tus necesidades
            val datosUsuario = hashMapOf(
                "nombre" to "",
                "correo" to user.email,
                // Agrega otros campos según sea necesario
            )

            // Guarda la información del usuario en Firestore
            usuarioDocRef.set(datosUsuario)
                .addOnSuccessListener {
                    Toast.makeText(
                        baseContext,
                        "Usuario creado correctamente.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
                .addOnFailureListener { e ->
                    // Fallo al guardar la información del usuario
                    // Manejar el error aquí
                    Toast.makeText(
                        baseContext,
                        "Error al crear el usuario.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
        }
    }
}