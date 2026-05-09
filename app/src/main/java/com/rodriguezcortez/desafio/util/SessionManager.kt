package com.rodriguezcortez.desafio.util

import android.content.Context

class SessionManager(context: Context) {

    private val pref =
        context.getSharedPreferences("session", Context.MODE_PRIVATE)

    fun saveUser(id: String, name: String, rol: String) {
        pref.edit()
            .putString("id", id)
            .putString("name", name)
            .putString("rol", rol)
            .putBoolean("isLogin", true)
            .apply()
    }

    fun isLogin(): Boolean {
        return pref.getBoolean("isLogin", false)
    }

    fun logout() {
        pref.edit().clear().apply()
    }
}