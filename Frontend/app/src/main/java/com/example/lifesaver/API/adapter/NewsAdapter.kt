package com.example.lifesaver.api.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lifesaver.R
import com.example.lifesaver.api.local.Helper
import com.example.lifesaver.api.model.News

class NewsAdapter(private val news: List<News>, private val onItemClick: (News) -> Unit) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {
    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.newsTitle)
        val image: ImageView = itemView.findViewById(R.id.newsImage)
        val created_at: TextView = itemView.findViewById(R.id.newsTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_news, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val newsItem = news[position]
        holder.name.text = newsItem.name
        Helper.ImageHelper.loadImageFromUrl(holder.image, "news/" + newsItem.image)
        holder.created_at.text = newsItem.created_at

        holder.itemView.setOnClickListener {
            onItemClick(newsItem)
        }
    }

    override fun getItemCount(): Int = news.size
}