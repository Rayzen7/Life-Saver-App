package com.example.lifesaver.api.model

data class Product(
    val id: Int,
    val image: String,
    val name: String,
    val category: String,
    val price: Int,
    val desc: String,
    val dosis: String,
    val quantity: Number
)