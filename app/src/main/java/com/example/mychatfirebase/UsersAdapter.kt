package com.example.mychatfirebase

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class UsersAdapter(val listaUsuarios: MutableList<Usuario>, val nombre: String) :
    RecyclerView.Adapter<UsuarioViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuarioViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return UsuarioViewHolder(layoutInflater.inflate(R.layout.user_item, parent, false));
    }

    override fun onBindViewHolder(holder: UsuarioViewHolder, position: Int) {
        val item = listaUsuarios[position]
        holder.render(item, nombre)
    }

    override fun getItemCount(): Int = listaUsuarios.size
}

class UsuarioViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val tvNombre: TextView = view.findViewById(R.id.tvUser)
    val itemLayout: CardView = view.findViewById(R.id.itemLayout)
    val context = view.context

    fun render(usuario: Usuario, nombre: String) {
        tvNombre.text = usuario.nombre

        itemLayout.setOnClickListener {
            val intent = Intent(context, ChatRoomActivity::class.java)
            intent.putExtra("nombre", usuario.nombre)
            intent.putExtra("userId", usuario.idUsuario)
            intent.putExtra("myName", nombre)

            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }
}