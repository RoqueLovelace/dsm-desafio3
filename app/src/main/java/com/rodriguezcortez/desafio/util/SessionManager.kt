package com.rodriguezcortez.desafio.util

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREF_NAME   = "session"
        private const val KEY_LOGIN   = "is_login"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_NAME    = "name"
        private const val KEY_ROL     = "rol"
    }

    fun saveUser(id: String, name: String, rol: String) {
        prefs.edit().apply {
            putBoolean(KEY_LOGIN, true)
            putString(KEY_USER_ID, id)
            putString(KEY_NAME, name)
            putString(KEY_ROL, rol)
            apply()
        }
    }

    fun isLogin(): Boolean = prefs.getBoolean(KEY_LOGIN, false)

    fun getUserId(): Int = prefs.getInt(KEY_USER_ID, -1)

    fun getName(): String = prefs.getString(KEY_NAME, "") ?: ""

    fun getRol(): String = prefs.getString(KEY_ROL, "user") ?: "user"

    fun logout() {
        prefs.edit().clear().apply()
    }
}