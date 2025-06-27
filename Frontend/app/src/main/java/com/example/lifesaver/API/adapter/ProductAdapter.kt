package com.example.lifesaver.api.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lifesaver.R
import com.example.lifesaver.api.local.Helper
import com.example.lifesaver.api.model.Product

class ProductAdapter(private val products: List<Product>, private val onItemClick: (Product) -> Unit) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {
    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.productName)
        val image: ImageView = itemView.findViewById(R.id.productImage)
        val category: TextView = itemView.findViewById(R.id.productCategory)
        val price: TextView = itemView.findViewById(R.id.productPrice)
        val productBtn: Button = itemView.findViewById(R.id.productBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.name.text = product.name
        holder.category.text = product.category
        holder.price.text = Helper.formatRupiah(product.price)
        Helper.ImageHelper.loadImageFromUrl(holder.image, "product/" + product.image)

        holder.productBtn.setOnClickListener {
            onItemClick(product)
        }
    }

    override fun getItemCount(): Int = products.size
}