package com.example.lifesaver

import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.lifesaver.api.HttpHandler
import com.example.lifesaver.api.local.Helper
import com.example.lifesaver.api.model.Product
import org.json.JSONObject

class DetailProduct : AppCompatActivity() {
    private lateinit var backBtn: ImageView
    private lateinit var buyBtn: Button
    private lateinit var cartBtn: ImageView
    private lateinit var buyBackground: LinearLayout
    private lateinit var backgroundProductDetail: ScrollView
    private lateinit var productDetailPayment: LinearLayout
    private lateinit var overlayBlocker: View

    private lateinit var quantityText: TextView
    private lateinit var quantityPlusBtn: ImageView
    private lateinit var quantityMinBtn: ImageView
    var quantity: Int = 0
    var price: Int = 0
    var productNamePut: String = ""
    var productIdPut: Int = 0

    private lateinit var productImage: ImageView
    private lateinit var productName: TextView
    private lateinit var productCategory: TextView
    private lateinit var productDesc: TextView
    private lateinit var productDosis: TextView
    private lateinit var productPrice: TextView

    private lateinit var productPaymentImage: ImageView
    private lateinit var productPaymentName: TextView
    private lateinit var productPaymentPrice: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.detail_product)

        backBtn = findViewById(R.id.backDetailProductBtn)
        buyBtn = findViewById(R.id.detailProductBuyBtn)
        cartBtn = findViewById(R.id.detailProductCart)
        buyBackground = findViewById(R.id.buyBackground)
        backgroundProductDetail = findViewById(R.id.backgroundProductDetail)
        productDetailPayment = findViewById(R.id.productDetailPayment)
        overlayBlocker = findViewById(R.id.productDetailOverlayBlocker)

        quantityText = findViewById(R.id.quantityText)
        quantityPlusBtn = findViewById(R.id.quantityPlusBtn)
        quantityMinBtn = findViewById(R.id.quantityMinBtn)

        productPaymentImage = findViewById(R.id.productDetailImagePayment)
        productPaymentName = findViewById(R.id.productDetailNamePayment)
        productPaymentPrice = findViewById(R.id.productDetailPricePayment)

        productImage = findViewById(R.id.productDetailImage)
        productName = findViewById(R.id.productDetailName)
        productCategory = findViewById(R.id.productDetailCategory)
        productDesc = findViewById(R.id.productDetailDesc)
        productDosis = findViewById(R.id.productDetailDosis)
        productPrice = findViewById(R.id.productDetailPrice)

        val productId = intent.getIntExtra("product_id", -1)
        if (productId != -1) {
            fetchProductDetail(productId)
        } else {
            finish()
        }

        backBtn.setOnClickListener() {
            finish()
        }

        buyBtn.setOnClickListener() {
            if (overlayBlocker.visibility == View.VISIBLE) {
                val intent = Intent(this@DetailProduct, MainTransactionAddress::class.java)
                intent.putExtra("id", productIdPut)
                intent.putExtra("name", productNamePut)
                intent.putExtra("quantity", quantity)
                intent.putExtra("total", quantity * price)
                startActivity(intent)
            } else {
                overlayBlocker.visibility = View.VISIBLE

                cartBtn.animate().alpha(0f).setDuration(200).withEndAction() {
                    cartBtn.visibility = View.GONE
                }.start()

                productDetailPayment.animate().alpha(1f).setDuration(200).withEndAction() {
                    productDetailPayment.visibility = View.VISIBLE
                    productDetailPayment.translationY = 0f
                }.start()

                backgroundProductDetail.animate().alpha(0.7f).setDuration(200).start()
                buyBackground.background = ContextCompat.getDrawable(this, R.drawable.rounded_top_white)

                buyBtn.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
                buyBtn.requestLayout()
            }
        }

        overlayBlocker.setOnClickListener() {
            overlayBlocker.visibility = View.GONE
            backgroundProductDetail.animate().alpha(1f).setDuration(200).start()

            buyBtn.layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
            buyBackground.background = ContextCompat.getDrawable(this, R.drawable.rounded_top)
            buyBtn.requestLayout()

            cartBtn.animate().alpha(1f).setDuration(200).withEndAction() {
                cartBtn.visibility = View.VISIBLE
            }.start()

            productDetailPayment.animate().alpha(0f).setDuration(200).withEndAction() {
                productDetailPayment.translationY = 300f
                productDetailPayment.visibility = View.GONE
            }.start()
        }

        quantityMain()
    }

    fun quantityMain() {
        quantity = 1
        quantityText.text = quantity.toString()

        quantityPlusBtn.setOnClickListener() {
            quantity++
            quantityText.text = quantity.toString()
        }

        quantityMinBtn.setOnClickListener() {
            if (quantity > 1) {
                quantity--
                quantityText.text = quantity.toString()
            }
        }
    }

    fun fetchProductDetail(productId: Int) {
        Thread {
            try {
                val response = HttpHandler().request("/product/${productId}", "GET")
                val jsonObject = JSONObject(response.body)
                val productObject = jsonObject.getJSONObject("product")

                val product = Product(
                    id = productObject.getInt("id"),
                    category = productObject.getJSONObject("category").getString("name"),
                    name = productObject.getString("name"),
                    image = productObject.getString("image"),
                    desc = productObject.getString("desc"),
                    dosis = productObject.getString("dosis"),
                    quantity = productObject.getInt("quantity"),
                    price = productObject.getInt("price")
                )

                runOnUiThread {
                    Helper.ImageHelper.loadImageFromUrl(productImage, "product/" + product.image)
                    productName.text = product.name
                    productDesc.text = product.desc
                    productDosis.text = product.dosis
                    productCategory.text = product.category
                    productPrice.text = Helper.formatRupiah(product.price)

                    productIdPut = product.id
                    price = product.price
                    productNamePut = product.name

                    productPaymentName.text = product.name
                    Helper.ImageHelper.loadImageFromUrl(productPaymentImage, "product/" + product.image)
                    productPaymentPrice.text = Helper.formatRupiah(product.price)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }
}