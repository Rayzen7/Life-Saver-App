package com.example.lifesaver

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.lifesaver.api.HttpHandler
import com.example.lifesaver.api.local.Helper
import com.example.lifesaver.api.model.News
import org.json.JSONObject

class DetailNews : AppCompatActivity() {
    private lateinit var newsTitle: TextView
    private lateinit var newsDesc: TextView
    private lateinit var newsImage: ImageView
    private lateinit var newsLink: Button
    private lateinit var backNewsDetail: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.detail_news)

        newsTitle = findViewById(R.id.newsDetailTitle)
        newsDesc = findViewById(R.id.newsDetailDesc)
        newsImage = findViewById(R.id.newsDetailImage)
        newsLink = findViewById(R.id.newsDetailLink)
        backNewsDetail = findViewById(R.id.newsDetailBack)

        val newsId = intent.getIntExtra("news_id", -1)
        if (newsId != -1) {
            fetchNewsDetail(newsId)
        } else {
            finish()
        }

        backNewsDetail.setOnClickListener {
            finish()
        }
    }

    fun fetchNewsDetail(newsId: Int) {
        Thread {
            try {
                val response = HttpHandler().request("/news/${newsId}", "GET", null)
                val jsonObject = JSONObject(response.body)
                val newsObject = jsonObject.getJSONObject("news")

                val news = News(
                    id = newsObject.getInt("id"),
                    name = newsObject.getString("name"),
                    desc = newsObject.getString("desc"),
                    link = newsObject.getString("link"),
                    user = newsObject.getJSONObject("user"),
                    created_at = Helper.formatDate(newsObject.getString("created_at")),
                    image = newsObject.getString("image")
                )

                runOnUiThread {
                    newsTitle.text = news.name
                    newsDesc.text = news.desc
                    Helper.ImageHelper.loadImageFromUrl(newsImage, "news/" + news.image)

                    newsLink.setOnClickListener {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(news.link))
                        startActivity(intent)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }
}