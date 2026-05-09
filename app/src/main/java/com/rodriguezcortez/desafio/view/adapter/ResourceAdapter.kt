package com.rodriguezcortez.desafio.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rodriguezcortez.desafio.R
import com.rodriguezcortez.desafio.model.Resource

class ResourceAdapter(
    private val listRes: List<Resource>,
    private val onClick: (Resource) -> Unit
) : RecyclerView.Adapter<ResourceAdapter.ResViewHolder>() {

    inner class ResViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {

        val imgRes: ImageView = view.findViewById(R.id.imgRes)
        val tvTit: TextView = view.findViewById(R.id.tvTit)
        val tvDesc: TextView = view.findViewById(R.id.tvDesc)
        val tvTipo: TextView = view.findViewById(R.id.tvTipo)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ResViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_resource, parent, false)

        return ResViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listRes.size
    }

    override fun onBindViewHolder(holder: ResViewHolder, position: Int) {

        val res = listRes[position]

        holder.tvTit.text = res.titulo
        holder.tvDesc.text = res.descripcion
        holder.tvTipo.text = res.tipo

        Glide.with(holder.itemView.context)
            .load(res.imagen)
            .into(holder.imgRes)

        holder.itemView.setOnClickListener {
            onClick(res)
        }
    }
}