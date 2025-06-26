package com.example.lifesaver.api.local

import android.content.Context

object MySharedPreference {
    val token = "token"
    val sharedToken = "sharedToken"

    fun saveToken(context: Context, tokenUser: String) {
        val shared = context.getSharedPreferences(sharedToken, Context.MODE_PRIVATE)
        with(shared.edit()) {
            putString(token, tokenUser)
            apply()
        }
    }

    fun getToken(context: Context): String? {
        val shared = context.getSharedPreferences(sharedToken, Context.MODE_PRIVATE)
        return shared.getString(token, null)
    }

    fun deleteToken(context: Context) {
        val shared = context.getSharedPreferences(sharedToken, Context.MODE_PRIVATE)
        with(shared.edit()) {
            remove(token)
            apply()
        }
    }

    val user = "user"
    val sharedUser = "sharedUser"

    fun saveUser(context: Context, username: String) {
        val shared = context.getSharedPreferences(sharedUser, Context.MODE_PRIVATE)
        with(shared.edit()) {
            putString(user, username)
            apply()
        }
    }

    fun getUser(context: Context): String? {
        val shared = context.getSharedPreferences(sharedUser, Context.MODE_PRIVATE)
        return shared.getString(user, null)
    }
}
