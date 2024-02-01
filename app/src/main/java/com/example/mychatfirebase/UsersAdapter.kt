package com.example.mychatfirebase

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class UsersAdapter(val listaUsuarios: MutableList<Usuario>) :
    RecyclerView.Adapter<UsuarioViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuarioViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return UsuarioViewHolder(layoutInflater.inflate(R.layout.user_item, parent, false));
    }

    override fun onBindViewHolder(holder: UsuarioViewHolder, position: Int) {
        val item = listaUsuarios[position]
        holder.render(item)
    }

    override fun getItemCount(): Int = listaUsuarios.size
}

class UsuarioViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val tvNombre: TextView = view.findViewById(R.id.tvUser)
    private val itemLayout: CardView = view.findViewById(R.id.itemLayout)
    val context: Context = view.context

    fun render(usuario: Usuario) {
        tvNombre.text = usuario.nombre

        itemLayout.setOnClickListener {
            val intent = Intent(context, ChatRoomActivity::class.java)
            intent.putExtra("otherUserName", usuario.nombre)
            intent.putExtra("otherUserID", usuario.idUsuario)
            context.startActivity(intent)
        }
    }
}