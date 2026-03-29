package com.donorconnect.api.v1.dto

/**
 * Data class representing the registration payload from the Android app.
 * Includes fields for both the User and Donor profiles.
 */
data class UserRegistrationRequest(
    val name: String,
    val email: String,
    val password: String,
    val contact: String,
    val location: String,
    val isDonor: Boolean,
    val bloodGroup: String?, // Optional if they are just a recipient
    val age: Int?,
    val gender: String?
)