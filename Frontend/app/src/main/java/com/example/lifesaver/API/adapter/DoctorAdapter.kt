package com.example.lifesaver.api.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lifesaver.R
import com.example.lifesaver.api.model.Doctor

class DoctorAdapter(private val doctor: List<Doctor>) : RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder>() {
    class DoctorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val doctorName: TextView = itemView.findViewById(R.id.doctorName)
        val doctorDesc: TextView = itemView.findViewById(R.id.doctorDesc)
        val chatDoctor: Button = itemView.findViewById(R.id.chatDoctor)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_doctor, parent, false)
        return DoctorViewHolder(view)
    }

    override fun onBindViewHolder(holder: DoctorViewHolder, position: Int) {
        val doctorItem = doctor[position]
        holder.doctorName.text = doctorItem.name
        holder.doctorDesc.text = doctorItem.desc
        holder.chatDoctor.setOnClickListener() {
            val phoneNumber = doctorItem.no_phone.toString()
            val context = holder.itemView.context
            val url = "https://wa.me/${phoneNumber}"

            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = doctor.size
}