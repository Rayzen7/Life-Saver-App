package com.example.lifesaver

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lifesaver.api.HttpHandler
import com.example.lifesaver.api.adapter.TransactionAdapter
import com.example.lifesaver.api.local.Helper
import com.example.lifesaver.api.local.MySharedPreference
import com.example.lifesaver.api.model.Transaction
import org.json.JSONObject

class HistoryFragment : Fragment(R.layout.fragment_history) {
    private lateinit var historyRecyclerView: RecyclerView
    private val historyList = mutableListOf<Transaction>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_history, container, false)
        historyRecyclerView = view.findViewById(R.id.historyAll)
        historyRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        fetchHistory()
        return view
    }

    fun fetchHistory() {
        Thread {
            try {
                val token = MySharedPreference.getToken(requireContext())
                val response = HttpHandler().request("/transaction", "GET", token)
                val jsonObject = JSONObject(response.body)
                val jsonArray = jsonObject.getJSONArray("transaction")

                for (i in 0  until jsonArray.length()) {
                    val item = jsonArray.getJSONObject(i)
                    historyList.add(
                        Transaction(
                            id = item.getInt("id"),
                            user_id = item.getInt("user_id"),
                            user = item.getJSONObject("user"),
                            product_id = item.getInt("product_id"),
                            product = item.getJSONObject("product"),
                            address = item.getString("address"),
                            status = item.getString("status"),
                            quantity = item.getInt("quantity"),
                            total = item.getInt("total"),
                            created_at = Helper.formatDate(item.getString("created_at")),
                            updated_at = Helper.formatDate(item.getString("updated_at")),
                        )
                    )
                }

                requireActivity().runOnUiThread() {
                    historyRecyclerView.adapter = TransactionAdapter(historyList) { historyItem ->
                        val intent = Intent(requireContext(), DetailHistory::class.java)
                        intent.putExtra("id", historyItem.id)
                        startActivity(intent)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }
}