package com.rodriguezcortez.desafio.view

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.rodriguezcortez.desafio.R
import com.rodriguezcortez.desafio.model.Resource
import com.rodriguezcortez.desafio.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResourceFormActivity : AppCompatActivity() {

    private lateinit var etTitulo: EditText
    private lateinit var etDesc: EditText
    private lateinit var etTipo: EditText
    private lateinit var etImg: EditText
    private lateinit var etLink: EditText
    private lateinit var rbFormRating: RatingBar
    private lateinit var btnGuardar: Button
    private lateinit var btnCancelar: Button

    private var modo  = "crear"
    private var resId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_resource)

        etTitulo    = findViewById(R.id.etTitulo)
        etDesc      = findViewById(R.id.etDesc)
        etTipo      = findViewById(R.id.etTipo)
        etImg       = findViewById(R.id.etImg)
        etLink      = findViewById(R.id.etLink)
        rbFormRating = findViewById(R.id.rbFormRating)
        btnGuardar  = findViewById(R.id.btnGuardar)
        btnCancelar = findViewById(R.id.btnCancelar)

        modo  = intent.getStringExtra("modo") ?: "crear"
        resId = intent.getIntExtra("id", -1)

        if (modo == "editar") {
            title = getString(R.string.tit_form_editar)
            etTitulo.setText(intent.getStringExtra("titulo")  ?: "")
            etDesc.setText(intent.getStringExtra("desc")      ?: "")
            etTipo.setText(intent.getStringExtra("tipo")      ?: "")
            etImg.setText(intent.getStringExtra("img")        ?: "")
            etLink.setText(intent.getStringExtra("link")      ?: "")
            rbFormRating.rating = intent.getFloatExtra("rating", 0f)
        } else {
            title = getString(R.string.tit_form_crear)
        }

        btnGuardar.setOnClickListener  { guardar() }
        btnCancelar.setOnClickListener { finish() }
    }

    private fun guardar() {
        val titulo = etTitulo.text.toString().trim()
        val desc   = etDesc.text.toString().trim()
        val tipo   = etTipo.text.toString().trim()
        val img    = etImg.text.toString().trim()
        val link   = etLink.text.toString().trim()
        val rating = rbFormRating.rating

        if (titulo.isEmpty()) { etTitulo.error = getString(R.string.val_required); etTitulo.requestFocus(); return }
        if (desc.isEmpty())   { etDesc.error   = getString(R.string.val_required); etDesc.requestFocus();   return }
        if (tipo.isEmpty())   { etTipo.error   = getString(R.string.val_required); etTipo.requestFocus();   return }
        if (img.isEmpty())    { etImg.error    = getString(R.string.val_required); etImg.requestFocus();    return }
        if (link.isEmpty())   { etLink.error   = getString(R.string.val_required); etLink.requestFocus();   return }

        val recurso = Resource(
            id          = if (resId.toString() != "-1") resId.toString() else "0",
            titulo      = titulo,
            descripcion = desc,
            tipo        = tipo,
            imagen      = img,
            enlace      = link,
            rating      = rating.toInt()
        )

        if (modo == "editar") actualizarRecurso(recurso) else crearRecurso(recurso)
    }

    private fun crearRecurso(recurso: Resource) {
        RetrofitClient.api.createResource(recurso)
            .enqueue(object : Callback<Resource> {
                override fun onResponse(call: Call<Resource>, response: Response<Resource>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@ResourceFormActivity, getString(R.string.msg_create_ok), Toast.LENGTH_SHORT).show()
                        terminarConExito()
                    } else {
                        Toast.makeText(this@ResourceFormActivity, getString(R.string.msg_error), Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<Resource>, t: Throwable) {
                    Toast.makeText(this@ResourceFormActivity, getString(R.string.msg_error), Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun actualizarRecurso(recurso: Resource) {
        RetrofitClient.api.updateResource(resId.toString(), recurso)
            .enqueue(object : Callback<Resource> {
                override fun onResponse(call: Call<Resource>, response: Response<Resource>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@ResourceFormActivity, getString(R.string.msg_update_ok), Toast.LENGTH_SHORT).show()
                        terminarConExito()
                    } else {
                        Toast.makeText(this@ResourceFormActivity, getString(R.string.msg_error), Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<Resource>, t: Throwable) {
                    Toast.makeText(this@ResourceFormActivity, getString(R.string.msg_error), Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun terminarConExito() {
        setResult(Activity.RESULT_OK)
        finish()
    }
}