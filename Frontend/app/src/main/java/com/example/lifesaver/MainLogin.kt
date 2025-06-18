package com.example.lifesaver

import android.content.Context
import android.content.Intent
import android.icu.text.CaseMap.Title
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.net.HttpURLConnection

class MainLogin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.login_main)

        val registerBtn = findViewById<Button>(R.id.registerBtn)
        val loginSuccess = findViewById<Button>(R.id.loginSuccess)

        registerBtn.setOnClickListener({
            val intent = Intent(this, MainRegister::class.java)
            startActivity(intent)
        })

        loginSuccess.setOnClickListener({
            Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show()

            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this, MainOnboarding::class.java)
                startActivity(intent)
                finish()
            }, 2000)
        })
    }
}

object PostLogin {
    fun postLogin(context: Context, name: String, password: String) {
        val handler = Handler(Looper.getMainLooper())

        Thread {
            try {

            } catch (e: Exception) {

            }
        }
    }
}