package com.example.lifesaver.api.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lifesaver.R
import com.example.lifesaver.api.model.Doctor

class DoctorAdapter(private val doctor: List<Doctor>, private val onItemClicl: (Doctor) -> Unit) {
    class DoctorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val name: TextView = itemView.findViewById(R.id.doctorName)
    }
}