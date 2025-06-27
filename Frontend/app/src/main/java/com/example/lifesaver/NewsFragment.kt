package com.example.lifesaver

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lifesaver.api.HttpHandler
import com.example.lifesaver.api.adapter.NewsAdapter
import com.example.lifesaver.api.local.Helper
import com.example.lifesaver.api.model.News
import org.json.JSONObject

class NewsFragment : Fragment(R.layout.fragment_news) {
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var newsRecyclerView: RecyclerView
    private val newsList = mutableListOf<News>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_news, container, false)
        newsRecyclerView = view.findViewById(R.id.newsAll)
        newsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        fetchNews()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val searchEditText = view.findViewById<EditText>(R.id.searchEditText)
        val bottomNav = requireActivity().findViewById<View>(R.id.bottomNavigationView)

        searchEditText.setOnFocusChangeListener {_, hasFocus ->
            if (hasFocus) {
                bottomNav.visibility = View.GONE
            } else {
                bottomNav.visibility = View.VISIBLE
            }
        }
    }

    fun fetchNews() {
        Thread {
            try {
                val response = HttpHandler().request("/news", "GET")
                val jsonObject = JSONObject(response.body)
                val jsonArray = jsonObject.getJSONArray("news")

                for (i in 0 until jsonArray.length()) {
                    val item = jsonArray.getJSONObject(i)
                    newsList.add(
                        News(
                            id = item.getInt("id"),
                            name = item.getString("name"),
                            desc = item.getString("desc"),
                            image = item.getString("image"),
                            user = item.getJSONObject("user"),
                            link = item.getString("link"),
                            created_at = Helper.formatDate(item.getString("created_at"))
                        )
                    )
                }

                requireActivity().runOnUiThread {
                    newsRecyclerView.adapter = NewsAdapter(newsList) { newsItem ->
                        val intent = Intent(requireContext(), DetailNews::class.java)
                        intent.putExtra("news_id", newsItem.id)
                        startActivity(intent)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }
}