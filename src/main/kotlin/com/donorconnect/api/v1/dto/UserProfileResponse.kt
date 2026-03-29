package com.donorconnect.api.v1.dto

import java.time.LocalDate

data class UserProfileResponse(
    val userId: Int,
    val name: String,
    val email: String,
    val contact: String,
    val location: String,
    val role: String,

    // New Medical/App-specific fields
    val isDonor: Boolean,
    val bloodGroup: String?,
    val age: Int? = null,
    val gender: String? = null,
    val isAvailable: Boolean? = null,
    val lastDonationDate: LocalDate? = null
)