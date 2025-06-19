package com.example.lifesaver.api.local

import android.content.Context
import android.util.Log
import android.widget.Toast

object Helper {
    fun log(string: String) {
        Log.e("DataApi", "Error : $string")
    }

    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
