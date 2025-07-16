package com.example.lifesaver

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.lifesaver.api.HttpHandler
import com.example.lifesaver.api.local.Helper
import com.example.lifesaver.api.local.MySharedPreference
import com.example.lifesaver.api.model.Transaction
import org.json.JSONArray
import org.json.JSONObject
import kotlin.concurrent.thread

class DetailHistory : AppCompatActivity() {
    private lateinit var productHistoryName: TextView
    private lateinit var productHistoryCategory: TextView
    private lateinit var productHistoryPrice: TextView
    private lateinit var productHistoryQuantity: TextView
    private lateinit var historyTotalPrice: TextView
    private lateinit var backBtn: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.detail_history)

        productHistoryName = findViewById(R.id.productHistoryName)
        productHistoryCategory = findViewById(R.id.productHistoryCategory)
        productHistoryQuantity = findViewById(R.id.productHistoryQuantity)
        productHistoryPrice = findViewById(R.id.productHistoryPrice)
        historyTotalPrice = findViewById(R.id.historyTotalPrice)
        backBtn = findViewById(R.id.detailHistoryBack)

        backBtn.setOnClickListener() {
            finish()
        }

        val Id = intent.getIntExtra("id", -1)
        if (Id != -1) {
            fetchTransaction(Id)
        } else {
            finish()
        }
    }

    fun fetchTransaction(transactionId: Int) {
        Thread {
            try {
                val token = MySharedPreference.getToken(this@DetailHistory)
                val response = HttpHandler().request("/transaction/${transactionId}", "GET", token)
                val JsonObject = JSONObject(response.body)
                val historyObject = JsonObject.getJSONObject("transaction")

                val history = Transaction(
                    id = historyObject.getInt("id"),
                    user_id = historyObject.getInt("user_id"),
                    user = historyObject.getJSONObject("user"),
                    product_id = historyObject.getInt("product_id"),
                    product = historyObject.getJSONObject("product"),
                    address = historyObject.getString("address"),
                    status = historyObject.getString("status"),
                    quantity = historyObject.getInt("quantity"),
                    total = historyObject.getInt("total"),
                    created_at = Helper.formatDate(historyObject.getString("created_at")),
                    updated_at = Helper.formatDate(historyObject.getString("updated_at"))
                )

                val totalPrice = history.total + 5000;
                runOnUiThread() {
                    productHistoryName.text = history.product.getString("name");
                    productHistoryCategory.text = history.product.getJSONObject("category").getString("name");
                    productHistoryQuantity.text = history.quantity.toString() + " Qty";
                    productHistoryPrice.text = Helper.formatRupiah(history.product.getInt("price"));
                    historyTotalPrice.text = Helper.formatRupiah(totalPrice);
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }
}