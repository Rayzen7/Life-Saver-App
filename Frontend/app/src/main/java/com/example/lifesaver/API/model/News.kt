package com.example.lifesaver.api.model

data class News(
    val id: Int,
    val name: String,
    val desc: String,
    val image: String,
    val link: String,
    val user: Any,
    val created_at: String,
)