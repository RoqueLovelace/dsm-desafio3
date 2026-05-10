package com.rodriguezcortez.desafio.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.rodriguezcortez.desafio.R
import com.rodriguezcortez.desafio.model.Resource
import com.rodriguezcortez.desafio.network.RetrofitClient
import com.rodriguezcortez.desafio.util.FavoritesManager
import com.rodriguezcortez.desafio.util.SessionManager
import com.rodriguezcortez.desafio.view.adapter.ResourceAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var rvRes: RecyclerView
    private lateinit var btnCerr: Button
    private lateinit var btnCrear: Button
    private lateinit var searchView: SearchView
    private lateinit var chipGroupTipos: ChipGroup
    private lateinit var chipFavs: Chip

    private lateinit var adapter: ResourceAdapter
    private lateinit var favManager: FavoritesManager

    private val crearLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) cargarResources()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvRes          = findViewById(R.id.rvRes)
        btnCerr        = findViewById(R.id.btnCerr)
        btnCrear       = findViewById(R.id.btnCrear)
        searchView     = findViewById(R.id.searchView)
        chipGroupTipos = findViewById(R.id.chipGroupTipos)
        chipFavs       = findViewById(R.id.chipFavs)

        rvRes.layoutManager = LinearLayoutManager(this)

        val sess = SessionManager(this)
        favManager = FavoritesManager(this, sess.getUserId())

        val rol = sess.getRol()
        btnCrear.visibility =
            if (rol == "admin" || rol == "docente") View.VISIBLE else View.GONE

        btnCrear.setOnClickListener {
            crearLauncher.launch(
                Intent(this, ResourceFormActivity::class.java).apply {
                    putExtra("modo", "crear")
                }
            )
        }

        btnCerr.setOnClickListener {
            sess.logout()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false
            override fun onQueryTextChange(newText: String?): Boolean {
                if (::adapter.isInitialized) adapter.filtrarPorTexto(newText ?: "")
                return true
            }
        })

        chipFavs.setOnCheckedChangeListener { _, isChecked ->
            if (::adapter.isInitialized) adapter.mostrarSoloFavoritos(isChecked)
        }
    }

    override fun onResume() {
        super.onResume()
        cargarResources()
    }

    private fun cargarResources() {
        RetrofitClient.api.getResources()
            .enqueue(object : Callback<List<Resource>> {

                override fun onResponse(
                    call: Call<List<Resource>>,
                    response: Response<List<Resource>>
                ) {
                    if (response.isSuccessful) {
                        val data = response.body() ?: emptyList()

                        adapter = ResourceAdapter(data, favManager) { res ->
                            startActivity(
                                Intent(this@MainActivity, DetailActivity::class.java).apply {
                                    putExtra("id",      res.id)
                                    putExtra("titulo",  res.titulo)
                                    putExtra("desc",    res.descripcion)
                                    putExtra("tipo",    res.tipo)
                                    putExtra("img",     res.imagen)
                                    putExtra("link",    res.enlace)
                                    putExtra("rating",  res.rating)
                                }
                            )
                        }
                        rvRes.adapter = adapter

                        val query = searchView.query?.toString() ?: ""
                        if (query.isNotEmpty()) adapter.filtrarPorTexto(query)
                        if (chipFavs.isChecked) adapter.mostrarSoloFavoritos(true)

                        construirChipsTipo(data)
                    }
                }

                override fun onFailure(call: Call<List<Resource>>, t: Throwable) {
                    Toast.makeText(
                        this@MainActivity,
                        getString(R.string.msg_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun construirChipsTipo(recursos: List<Resource>) {
        chipGroupTipos.removeAllViews()

        val tipos = listOf("") + recursos.map { it.tipo }.distinct().sorted()

        tipos.forEach { tipo ->
            val chip = Chip(this).apply {
                text = if (tipo.isEmpty()) getString(R.string.chip_todos) else tipo
                isCheckable = true
                isChecked   = tipo.isEmpty()
            }
            chip.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked && ::adapter.isInitialized) {
                    adapter.filtrarPorTipo(tipo)
                }
            }
            chipGroupTipos.addView(chip)
        }
    }
}