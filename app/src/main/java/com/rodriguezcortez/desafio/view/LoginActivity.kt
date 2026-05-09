package com.rodriguezcortez.desafio.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.rodriguezcortez.desafio.R
import com.rodriguezcortez.desafio.model.User
import com.rodriguezcortez.desafio.network.RetrofitClient
import com.rodriguezcortez.desafio.util.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var etMail: EditText
    private lateinit var etPass: EditText
    private lateinit var btnLogi: Button
    private lateinit var btnRegi: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sess = SessionManager(this)

        if (sess.isLogin()) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        setContentView(R.layout.activity_login)

        etMail = findViewById(R.id.etMail)
        etPass = findViewById(R.id.etPass)
        btnLogi = findViewById(R.id.btnLogi)
        btnRegi = findViewById(R.id.btnRegi)

        btnLogi.setOnClickListener {
            login()
        }

        btnRegi.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun login() {

        val mail = etMail.text.toString().trim()
        val pass = etPass.text.toString().trim()

        RetrofitClient.api.getUsers()
            .enqueue(object : Callback<List<User>> {

                override fun onResponse(
                    call: Call<List<User>>,
                    response: Response<List<User>>
                ) {

                    if (response.isSuccessful) {

                        val user = response.body()?.find {
                            it.correo == mail && it.password == pass
                        }

                        if (user != null) {

                            SessionManager(this@LoginActivity)
                                .saveUser(user.id, user.name, user.rol)

                            startActivity(
                                Intent(
                                    this@LoginActivity,
                                    MainActivity::class.java
                                )
                            )

                            finish()

                        } else {

                            Toast.makeText(
                                this@LoginActivity,
                                getString(R.string.msg_login_error),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

                override fun onFailure(call: Call<List<User>>, t: Throwable) {

                    Toast.makeText(
                        this@LoginActivity,
                        getString(R.string.msg_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}