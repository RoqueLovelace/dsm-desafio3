package com.rodriguezcortez.desafio.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        rvRes = findViewById(R.id.rvRes)
        btnCerr = findViewById(R.id.btnCerr)

        rvRes.layoutManager = LinearLayoutManager(this)

        cargarResources()

        btnCerr.setOnClickListener {

            SessionManager(this).logout()

            startActivity(
                Intent(this, LoginActivity::class.java)
            )

            finish()
        }
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

                            val inte =
                                Intent(
                                    this@MainActivity,
                                    DetailActivity::class.java
                                )

                            inte.putExtra("titulo", res.titulo)
                            inte.putExtra("desc", res.descripcion)
                            inte.putExtra("tipo", res.tipo)
                            inte.putExtra("img", res.imagen)
                            inte.putExtra("link", res.enlace)

                            startActivity(inte)
                        }
                    }
                }

                override fun onFailure(
                    call: Call<List<Resource>>,
                    t: Throwable
                ) {

                    Toast.makeText(
                        this@MainActivity,
                        getString(R.string.msg_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}