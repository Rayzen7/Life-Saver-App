package com.example.lifesaver.api.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lifesaver.R
import com.example.lifesaver.api.local.Helper
import com.example.lifesaver.api.model.Transaction

class TransactionAdapter(private val transaction: List<Transaction>, private val onItemClick: (Transaction) -> Unit) : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {
    class TransactionViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.historyName)
        val createdAt: TextView = itemView.findViewById(R.id.historyCreated)
        val price: TextView = itemView.findViewById(R.id.historyPrice)
        val historyBtn: LinearLayout = itemView.findViewById(R.id.histotyBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transactionItem = transaction[position]
        holder.name.text = transactionItem.product.getString("name");
        holder.createdAt.text = transactionItem.created_at.toString();
        holder.price.text = "-" + Helper.formatRupiah(transactionItem.total);

        holder.historyBtn.setOnClickListener() {
            onItemClick(transactionItem)
        }
    }

    override fun getItemCount(): Int = transaction.size
}