package com.rodriguezcortez.desafio.util

import android.content.Context
import android.content.SharedPreferences

class FavoritesManager(context: Context, userId: String) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("favs_user_$userId", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_FAVS = "favorites"
    }

    private fun getIds(): MutableSet<String> =
        prefs.getStringSet(KEY_FAVS, emptySet())?.toMutableSet() ?: mutableSetOf()

    fun isFavorite(resourceId: String): Boolean = getIds().contains(resourceId.toString())

    fun toggle(resourceId: String): Boolean {
        val ids = getIds()
        val wasFav = ids.contains(resourceId.toString())
        if (wasFav) ids.remove(resourceId.toString()) else ids.add(resourceId.toString())
        prefs.edit().putStringSet(KEY_FAVS, ids).apply()
        return !wasFav
    }

    fun getFavoriteIds(): Set<String> = getIds().map { it.toString() }.toSet()
}