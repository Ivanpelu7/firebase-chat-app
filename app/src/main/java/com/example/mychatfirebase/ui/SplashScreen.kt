package com.example.mychatfirebase.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.example.mychatfirebase.R
import com.example.mychatfirebase.util.FirebaseUtil
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        // En la aplicación
        Firebase.firestore.clearPersistence()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    private fun comprobarSesion() {
        if (Firebase.auth.currentUser != null) {
            FirebaseUtil.getCurrentUserDocumentRef()
                .get()
                .addOnSuccessListener {
                    val nombre = it.getString("nombre")

                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("nombre", nombre)
                    startActivity(intent)
                    finish()
                }


        } else {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        comprobarSesion()
    }
}