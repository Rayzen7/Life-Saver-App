package com.example.lifesaver

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lifesaver.api.HttpHandler
import com.example.lifesaver.api.adapter.ProductAdapter
import com.example.lifesaver.api.model.Product
import org.json.JSONObject

class ProductAll : AppCompatActivity() {
    private lateinit var productRecyclerView: RecyclerView
    private val productList = mutableListOf<Product>()
    private lateinit var productAllBack: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.all_product)

        productAllBack = findViewById(R.id.allProductBack)
        productAllBack.setOnClickListener {
            finish()
        }

        productRecyclerView = findViewById(R.id.productAll)
        productRecyclerView.layoutManager = GridLayoutManager(this@ProductAll, 2)
        fetchProduct()
    }

    fun fetchProduct() {
        Thread {
            try {
                val response = HttpHandler().request("/product", "GET")
                val jsonObject = JSONObject(response.body)
                val jsonProduct = jsonObject.getJSONArray("product")

                for (i in 0 until jsonProduct.length()) {
                    val item = jsonProduct.getJSONObject(i)
                    productList.add(
                        Product(
                            id = item.getInt("id"),
                            name = item.getString("name"),
                            desc = item.getString("desc"),
                            image = item.getString("image"),
                            category = item.getJSONObject("category").getString("name"),
                            price = item.getInt("price"),
                            dosis = item.getString("dosis"),
                            quantity = item.getInt("quantity"),
                        )
                    )
                }

                runOnUiThread {
                    productRecyclerView.adapter = ProductAdapter(productList) { productItem ->
                        val intent = Intent(this@ProductAll, DetailProduct::class.java)
                        intent.putExtra("product_id", productItem.id)
                        startActivity(intent)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }
}