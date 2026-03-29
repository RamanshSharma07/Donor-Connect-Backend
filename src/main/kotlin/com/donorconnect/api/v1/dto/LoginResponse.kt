package com.donorconnect.api.v1.dto

data class LoginResponse(
    val token: String,
    val userId: Int,
    val name: String,
    val role: String,
    val message: String = "Login successful"
)