package com.example.lifesaver

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class SuccessTransaction : AppCompatActivity() {
    private lateinit var btnSuccess: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.transaction_success)

        btnSuccess = findViewById(R.id.transactionSuccess)
        btnSuccess.setOnClickListener() {
            val intent = Intent(this@SuccessTransaction, MainHome::class.java)
            startActivity(intent)
            finish()
        }
    }
}