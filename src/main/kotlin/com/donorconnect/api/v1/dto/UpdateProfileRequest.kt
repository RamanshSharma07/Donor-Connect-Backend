package com.donorconnect.api.v1.dto

import java.time.LocalDate

data class UpdateProfileRequest(
    val contact: String?,
    val location: String?,

    // Donor-specific fields (nullable so Recipients can still use this DTO)
    val isAvailable: Boolean?,
    val lastDonationDate: LocalDate?
)