package com.rodriguezcortez.desafio.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.rodriguezcortez.desafio.R

class DetailActivity : AppCompatActivity() {

    private lateinit var imgDet: ImageView
    private lateinit var tvTitDet: TextView
    private lateinit var tvTipoDet: TextView
    private lateinit var tvDescDet: TextView
    private lateinit var btnAbrir: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_detail)

        imgDet = findViewById(R.id.imgDet)
        tvTitDet = findViewById(R.id.tvTitDet)
        tvTipoDet = findViewById(R.id.tvTipoDet)
        tvDescDet = findViewById(R.id.tvDescDet)
        btnAbrir = findViewById(R.id.btnAbrir)

        val titulo = intent.getStringExtra("titulo")
        val desc = intent.getStringExtra("desc")
        val tipo = intent.getStringExtra("tipo")
        val img = intent.getStringExtra("img")
        val link = intent.getStringExtra("link")

        tvTitDet.text = titulo
        tvTipoDet.text = tipo
        tvDescDet.text = desc

        Glide.with(this)
            .load(img)
            .into(imgDet)

        btnAbrir.setOnClickListener {

            val inte = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(link)
            )

            startActivity(inte)
        }
    }
}