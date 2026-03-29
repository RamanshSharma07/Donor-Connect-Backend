package com.donorconnect.api.v1.dto

data class ResetPasswordRequest(
    val email: String,
    val otpCode: String,
    val newPassword: String
)