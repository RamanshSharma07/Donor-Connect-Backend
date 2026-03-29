package com.donorconnect.api.v1.dto

data class VerifyOtpRequest(
    val email: String,
    val otpCode: String
)