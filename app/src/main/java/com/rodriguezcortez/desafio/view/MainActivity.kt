package com.rodriguezcortez.desafio.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rodriguezcortez.desafio.R
import com.rodriguezcortez.desafio.model.Resource
import com.rodriguezcortez.desafio.network.RetrofitClient
import com.rodriguezcortez.desafio.util.SessionManager
import com.rodriguezcortez.desafio.view.adapter.ResourceAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var rvRes: RecyclerView
    private lateinit var btnCerr: Button
    private lateinit var btnCrear: Button

    private val crearLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                cargarResources()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvRes    = findViewById(R.id.rvRes)
        btnCerr  = findViewById(R.id.btnCerr)
        btnCrear = findViewById(R.id.btnCrear)

        rvRes.layoutManager = LinearLayoutManager(this)

        val rol = SessionManager(this).getRol()
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
            SessionManager(this).logout()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
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
                        rvRes.adapter = ResourceAdapter(data) { res ->
                            startActivity(
                                Intent(this@MainActivity, DetailActivity::class.java).apply {
                                    putExtra("id",     res.id)
                                    putExtra("titulo", res.titulo)
                                    putExtra("desc",   res.descripcion)
                                    putExtra("tipo",   res.tipo)
                                    putExtra("img",    res.imagen)
                                    putExtra("link",   res.enlace)
                                }
                            )
                        }
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
}