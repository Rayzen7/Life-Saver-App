package com.example.lifesaver.api.model

import org.json.JSONObject

data class Transaction(
    val id: Int,
    val user_id: Int,
    val user: JSONObject,
    val product_id: Int,
    val product: JSONObject,
    val address: String,
    val status: String,
    val quantity: Int,
    val total: Int,
    val created_at: Any,
    val updated_at: Any,
)