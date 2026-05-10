package com.rodriguezcortez.desafio.view

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.rodriguezcortez.desafio.R
import com.rodriguezcortez.desafio.network.RetrofitClient
import com.rodriguezcortez.desafio.util.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {

    private lateinit var imgDet: ImageView
    private lateinit var tvTitDet: TextView
    private lateinit var tvTipoDet: TextView
    private lateinit var tvDescDet: TextView
    private lateinit var btnAbrir: Button
    private lateinit var btnEditar: Button
    private lateinit var btnEliminar: Button

    private val formLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                finish()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        imgDet     = findViewById(R.id.imgDet)
        tvTitDet   = findViewById(R.id.tvTitDet)
        tvTipoDet  = findViewById(R.id.tvTipoDet)
        tvDescDet  = findViewById(R.id.tvDescDet)
        btnAbrir   = findViewById(R.id.btnAbrir)
        btnEditar  = findViewById(R.id.btnEditar)
        btnEliminar = findViewById(R.id.btnEliminar)

        val titulo = intent.getStringExtra("titulo") ?: ""
        val desc   = intent.getStringExtra("desc")   ?: ""
        val tipo   = intent.getStringExtra("tipo")   ?: ""
        val img    = intent.getStringExtra("img")    ?: ""
        val link   = intent.getStringExtra("link")   ?: ""
        val resId  = intent.getStringExtra("id") ?: "-1"

        tvTitDet.text  = titulo
        tvTipoDet.text = tipo
        tvDescDet.text = desc

        Glide.with(this).load(img).into(imgDet)

        val rol = SessionManager(this).getRol()
        if (rol == "admin" || rol == "docente") {
            btnEditar.visibility   = View.VISIBLE
            btnEliminar.visibility = View.VISIBLE
        } else {
            btnEditar.visibility   = View.GONE
            btnEliminar.visibility = View.GONE
        }

        btnAbrir.setOnClickListener {
            if (link.isNotEmpty()) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
            } else {
                Toast.makeText(this, getString(R.string.msg_no_link), Toast.LENGTH_SHORT).show()
            }
        }

        btnEditar.setOnClickListener {
            val inte = Intent(this, ResourceFormActivity::class.java).apply {
                putExtra("modo",   "editar")
                putExtra("id",     resId)
                putExtra("titulo", titulo)
                putExtra("desc",   desc)
                putExtra("tipo",   tipo)
                putExtra("img",    img)
                putExtra("link",   link)
            }
            formLauncher.launch(inte)
        }

        btnEliminar.setOnClickListener {
            if (resId == "-1") {
                Toast.makeText(this, getString(R.string.msg_error), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            AlertDialog.Builder(this)
                .setTitle(getString(R.string.dialog_del_title))
                .setMessage(getString(R.string.dialog_del_msg))
                .setPositiveButton(getString(R.string.dialog_del_confirm)) { _, _ ->
                    eliminarRecurso(resId)
                }
                .setNegativeButton(getString(R.string.dialog_del_cancel), null)
                .show()
        }
    }

    private fun eliminarRecurso(id: String) {
        RetrofitClient.api.deleteResource(id)
            .enqueue(object : Callback<Void> {

                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@DetailActivity,
                            getString(R.string.msg_del_ok),
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    } else {
                        Toast.makeText(
                            this@DetailActivity,
                            getString(R.string.msg_error),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(
                        this@DetailActivity,
                        getString(R.string.msg_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}