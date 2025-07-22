package com.example.lifesaver

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lifesaver.api.HttpHandler
import com.example.lifesaver.api.adapter.DoctorAdapter
import com.example.lifesaver.api.model.Doctor
import org.json.JSONObject

class MainDoctor : AppCompatActivity() {
    private lateinit var doctorRecyclerView: RecyclerView
    private val doctorList = mutableListOf<Doctor>()
    private lateinit var doctorBack: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.doctor_main)

        doctorRecyclerView = findViewById(R.id.doctorAll)
        doctorBack = findViewById(R.id.doctorBack)

        doctorRecyclerView.layoutManager = LinearLayoutManager(this@MainDoctor)
        doctorRecyclerView.adapter = DoctorAdapter(doctorList)

        doctorBack.setOnClickListener() {
            val intent = Intent(this@MainDoctor, HomeFragment::class.java)
            startActivity(intent)
        }

        fetchDoctor()
    }

    fun fetchDoctor() {
        Thread {
            try {
                val response = HttpHandler().request("/doctor", "GET")
                val body = JSONObject(response.body)
                val bodyArray = body.getJSONArray("doctor")

                for (i in 0 until bodyArray.length()) {
                    val item = bodyArray.getJSONObject(i)
                    doctorList.add(
                        Doctor(
                            id = item.getInt("id"),
                            name = item.getString("name"),
                            desc = item.getString("desc"),
                            no_phone = item.getInt("no_phone")
                        )
                    )
                }

                runOnUiThread() {
                    doctorRecyclerView.adapter = DoctorAdapter(doctorList)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }
}