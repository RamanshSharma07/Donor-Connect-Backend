package com.donorconnect.api.v1.dto

data class TokenRefreshResponse(
    val accessToken: String,
    val refreshToken: String
)