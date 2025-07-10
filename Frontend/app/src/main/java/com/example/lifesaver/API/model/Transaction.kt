package com.example.lifesaver.api.model

data class Transaction(
    val id: Number,
    val user_id: Number,
    val user: User,
    val product_id: Number,
    val product: Product,
    val address: String,
    val status: String,
    val quantity: Number,
    val total: Number,
    val created_at: Any,
    val updated_at: Any,
)