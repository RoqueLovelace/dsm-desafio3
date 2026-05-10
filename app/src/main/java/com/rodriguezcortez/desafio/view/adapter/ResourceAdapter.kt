package com.rodriguezcortez.desafio.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rodriguezcortez.desafio.R
import com.rodriguezcortez.desafio.model.Resource
import com.rodriguezcortez.desafio.util.FavoritesManager

class ResourceAdapter(
    private val fullList: List<Resource>,
    private val favManager: FavoritesManager,
    private val onClick: (Resource) -> Unit
) : RecyclerView.Adapter<ResourceAdapter.ViewHolder>() {

    private var filtered: List<Resource> = fullList.toList()

    private var queryText  = ""
    private var queryTipo  = ""
    private var soloFavs   = false

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgRes: ImageView   = view.findViewById(R.id.imgRes)
        val tvTit: TextView     = view.findViewById(R.id.tvTit)
        val tvDesc: TextView    = view.findViewById(R.id.tvDesc)
        val tvTipo: TextView    = view.findViewById(R.id.tvTipo)
        val rbRating: RatingBar = view.findViewById(R.id.rbRating)
        val ivFav: ImageView    = view.findViewById(R.id.ivFav)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_resource, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val res = filtered[position]
        val ctx = holder.itemView.context

        holder.tvTit.text   = res.titulo
        holder.tvDesc.text  = res.descripcion
        holder.tvTipo.text  = res.tipo
        holder.rbRating.rating = res.rating.toFloat()

        Glide.with(ctx).load(res.imagen).into(holder.imgRes)

        holder.ivFav.setImageResource(
            if (favManager.isFavorite(res.id)) R.drawable.ic_fav_filled
            else R.drawable.ic_fav_outline
        )
        holder.ivFav.setOnClickListener {
            favManager.toggle(res.id)
            notifyItemChanged(position)
            if (soloFavs) aplicarFiltros()
        }

        holder.itemView.setOnClickListener { onClick(res) }
    }

    override fun getItemCount(): Int = filtered.size

    fun filtrarPorTexto(query: String) {
        queryText = query.trim().lowercase()
        aplicarFiltros()
    }

    fun filtrarPorTipo(tipo: String) {
        queryTipo = tipo
        aplicarFiltros()
    }

    fun mostrarSoloFavoritos(activo: Boolean) {
        soloFavs = activo
        aplicarFiltros()
    }

    private fun aplicarFiltros() {
        filtered = fullList.filter { res ->
            val matchTexto = queryText.isEmpty() ||
                    res.id.toString().contains(queryText) ||
                    res.titulo.lowercase().contains(queryText) ||
                    res.tipo.lowercase().contains(queryText)

            val matchTipo = queryTipo.isEmpty() ||
                    res.tipo.equals(queryTipo, ignoreCase = true)

            val matchFav = !soloFavs || favManager.isFavorite(res.id)

            matchTexto && matchTipo && matchFav
        }
        notifyDataSetChanged()
    }
}