package com.example.lifesaver.api.local

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import java.net.URL
import java.text.NumberFormat
import java.util.Locale
import kotlin.concurrent.thread

object Helper {
    fun log(string: String) {
        Log.e("DataApi", "Error : $string")
    }

    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    object ImageHelper {
        private const val BASE_URL = "http://10.0.2.2:3000/uploads/"

        fun loadImageFromUrl(imageView: ImageView, filename: String) {
            thread {
                try {
                    val fullUrl = BASE_URL + filename
                    val input = URL(fullUrl).openStream()
                    val bitmap = BitmapFactory.decodeStream(input)
                    imageView.post {
                        imageView.setImageBitmap(bitmap)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    imageView.post {
                        imageView.setImageResource(android.R.drawable.ic_dialog_alert)
                    }
                }
            }
        }
    }

    fun formatRupiah(amount: Int): String {
        val format = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
        return format.format(amount).replace(",00", "")
    }

    fun formatDate(dateString: String): String {
        return try {
            dateString.substring(0, 10)
        } catch (e: Exception) {
            dateString
        }
    }
}
