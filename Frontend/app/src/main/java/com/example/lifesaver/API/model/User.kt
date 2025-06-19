package com.example.lifesaver.api.model

data class User (
    val id: Int? = null,
    val username: String? = null,
    val email: String? = null,
    val password: String? = null,
    val nik: Number? = null,
    val phone: Number? = null,
    val role: String? = null
)