package com.example.lifesaver

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainRegister : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.register_main)

        val loginBtn = findViewById<Button>(R.id.loginBtn)
        loginBtn.setOnClickListener({
            val intent = Intent(this, MainLogin::class.java)
            startActivity(intent)
        })
    }
}