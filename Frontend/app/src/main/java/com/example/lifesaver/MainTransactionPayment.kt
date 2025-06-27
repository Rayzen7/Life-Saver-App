package com.example.lifesaver

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.lifesaver.api.HttpHandler
import com.example.lifesaver.api.local.Helper
import com.example.lifesaver.api.local.MySharedPreference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class MainTransactionPayment : AppCompatActivity() {
    private lateinit var radioDana: RadioButton
    private lateinit var radioCard: RadioButton
    private lateinit var radioPulsa: RadioButton
    private lateinit var backBtn: ImageView
    private lateinit var successBtn: Button

    private lateinit var productName: TextView
    private lateinit var productQuantity: TextView
    private lateinit var productPrice: TextView

    var productPaymentId: Int = 0
    var productPaymentName: String? = ""
    var productPaymentQuantity: Int = 0
    var productPaymentPrice: Int = 0
    var userAddress: String? = ""

    private fun clearAll() {
        radioDana.isChecked = false
        radioCard.isChecked = false
        radioPulsa.isChecked = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.transaction_payment_main)

        radioDana = findViewById(R.id.radioDana)
        radioCard = findViewById(R.id.radioCard)
        radioPulsa = findViewById(R.id.radioPulsa)
        backBtn = findViewById(R.id.transactionPaymentBackBtn)
        successBtn = findViewById(R.id.transactionPaymentBtnNext)

        productName = findViewById(R.id.paymentName)
        productPrice = findViewById(R.id.paymentPrice)
        productQuantity = findViewById(R.id.paymentQuantity)

        productPaymentId = intent.getIntExtra("id", -1)
        productPaymentName = intent.getStringExtra("name")
        productPaymentQuantity = intent.getIntExtra("quantity", -1)
        productPaymentPrice = intent.getIntExtra("total", -1)
        userAddress = intent.getStringExtra("address")

        radioDana.setOnClickListener {
            clearAll()
            radioDana.isChecked = true
        }

        radioCard.setOnClickListener {
            clearAll()
            radioCard.isChecked = true
        }

        radioPulsa.setOnClickListener {
            clearAll()
            radioPulsa.isChecked = true
        }

        backBtn.setOnClickListener() {
            finish()
        }

        fetchTransactionPayment()
        successBtn.setOnClickListener() {
            handlePayment(productPaymentId)
        }
    }

    fun fetchTransactionPayment() {
        Thread {
            try {
                runOnUiThread() {
                    productName.text = productPaymentName
                    productQuantity.text = "${productPaymentQuantity.toString()} Product"
                    productPrice.text = Helper.formatRupiah(productPaymentPrice)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }

    fun handlePayment(productId: Int) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val json = JSONObject().apply {
                    put("product_id", productId)
                    put("address", userAddress.toString())
                    put("quantity", productPaymentQuantity)
                    put("total", productPaymentPrice)
                }

                val token = MySharedPreference.getToken(this@MainTransactionPayment)
                val response = HttpHandler().request("/transaction", "POST", token, json.toString())
                val message = JSONObject(response.body).getString("message")

                if (response.code in 200..300) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@MainTransactionPayment, "Loading", Toast.LENGTH_SHORT).show()
                        Handler(Looper.getMainLooper()).postDelayed({
                            Toast.makeText(this@MainTransactionPayment, message, Toast.LENGTH_SHORT).show()
                        }, 2000)

                        Handler(Looper.getMainLooper()).postDelayed({
                            val intent = Intent(this@MainTransactionPayment, SuccessTransaction::class.java)
                            startActivity(intent)
                        }, 3000)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}