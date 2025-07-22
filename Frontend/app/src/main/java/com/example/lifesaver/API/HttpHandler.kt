package com.example.lifesaver.api

import com.example.lifesaver.api.model.HttpModel
import com.example.lifesaver.api.local.Helper
import java.net.HttpURLConnection
import java.net.URL

class HttpHandler {
//    val baseUrl = "http://10.0.2.2:3000"
    val baseUrl = "https://lifesaver-backend.setionugraha.my.id"

    fun request(
        endpoint: String,
        method: String? = "GET",
        token: String? = null,
        rBody: String? = null
    ): HttpModel {
        var code: Int? = null
        var body: String? = null

        try {
            val url = URL(baseUrl + endpoint)
            val conn = url.openConnection() as HttpURLConnection
            conn.setRequestProperty("Content-Type", "application/json")
            conn.requestMethod = method

            if (token != null) {
                conn.setRequestProperty("Authorization", "Bearer ${token}")
            }

            if (rBody != null) {
                conn.doOutput = true
                conn.outputStream.use { it.write(rBody.toByteArray()) }
            }

            code = conn.responseCode
            body = try {
                conn.inputStream.bufferedReader().use { it.readText() }
            } catch (e: Exception) {
                conn.errorStream.bufferedReader().use { it.readText() }
            }
        } catch (e: Exception) {
            Helper.log(e.message!!)
        }

        return HttpModel(code!!, body!!)
    }
}