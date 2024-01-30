package com.example.mychatfirebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object FirebaseUtil {

    fun getCurrentUserID(): String {
        return Firebase.auth.currentUser!!.uid
    }

    fun getCurrentUserDocumentRef() : DocumentReference {
        return Firebase.firestore.collection("users").document(getCurrentUserID())
    }

    fun getChatsRef() : CollectionReference {
        return Firebase.firestore.collection("chats")
    }
}