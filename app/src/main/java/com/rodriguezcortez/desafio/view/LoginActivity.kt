package com.rodriguezcortez.desafio.view

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
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
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sess = SessionManager(this)

        if (sess.isLogin()) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_login)

        initViews()
        initListeners()
    }

    private fun initViews() {

        etMail = findViewById(R.id.etMail)
        etPass = findViewById(R.id.etPass)
        btnLogi = findViewById(R.id.btnLogi)
        btnRegi = findViewById(R.id.btnRegi)
        progressBar = findViewById(R.id.progressBar)
    }

    private fun initListeners() {

        btnLogi.setOnClickListener {
            validarCampos()
        }

        btnRegi.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun validarCampos() {

        val mail = etMail.text.toString().trim()
        val pass = etPass.text.toString().trim()

        if (mail.isEmpty()) {
            etMail.error = getString(R.string.val_required)
            etMail.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
            etMail.error = getString(R.string.val_mail_invalid)
            etMail.requestFocus()
            return
        }

        if (pass.isEmpty()) {
            etPass.error = getString(R.string.val_required)
            etPass.requestFocus()
            return
        }

        login(mail, pass)
    }

    private fun login(mail: String, pass: String) {

        loading(true)

        RetrofitClient.api.getUsers()
            .enqueue(object : Callback<List<User>> {

                override fun onResponse(
                    call: Call<List<User>>,
                    response: Response<List<User>>
                ) {

                    loading(false)

                    if (response.isSuccessful) {

                        val user = response.body()?.find {
                            it.correo == mail && it.password == pass
                        }

                        if (user != null) {

                            SessionManager(this@LoginActivity)
                                .saveUser(
                                    user.id,
                                    user.name,
                                    user.rol
                                )

                            Toast.makeText(
                                this@LoginActivity,
                                getString(R.string.msg_login_ok),
                                Toast.LENGTH_SHORT
                            ).show()

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

                    } else {

                        Toast.makeText(
                            this@LoginActivity,
                            "ERROR ${response.code()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(
                    call: Call<List<User>>,
                    t: Throwable
                ) {

                    loading(false)

                    Toast.makeText(
                        this@LoginActivity,
                        t.message ?: getString(R.string.msg_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun loading(state: Boolean) {

        progressBar.visibility =
            if (state) View.VISIBLE else View.GONE

        btnLogi.isEnabled = !state
        btnRegi.isEnabled = !state
    }
}