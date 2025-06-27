package com.example.lifesaver

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainTransactionAddress : AppCompatActivity() {
    private lateinit var backBtn: ImageView
    private lateinit var nextBtn: Button

    private lateinit var userAddress: EditText
    private lateinit var userName: EditText
    private lateinit var userPhone: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.transaction_address_main)

        val productQuantity = intent.getIntExtra("quantity", -1)
        val productTotal = intent.getIntExtra("total", -1)
        val productName = intent.getStringExtra("name")
        val productId = intent.getIntExtra("id", -1)

        backBtn = findViewById(R.id.transactionAddressBackBtn)
        nextBtn = findViewById(R.id.transactionAddressBtnNext)

        userAddress = findViewById(R.id.transactionAddress)
        userName = findViewById(R.id.transactionName)
        userPhone = findViewById(R.id.transactionPhone)

        backBtn.setOnClickListener() {
            finish()
        }

        nextBtn.setOnClickListener() {
            if (userAddress.text.toString() == "" || userName.text.toString() == "" || userPhone.text.toString() == "") {
                Toast.makeText(this@MainTransactionAddress, "Field must be required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(this@MainTransactionAddress, MainTransactionPayment::class.java)
            intent.putExtra("address", userAddress.text.toString())
            intent.putExtra("quantity", productQuantity)
            intent.putExtra("total", productTotal)
            intent.putExtra("name", productName)
            intent.putExtra("id", productId)
            startActivity(intent)
        }
    }
}