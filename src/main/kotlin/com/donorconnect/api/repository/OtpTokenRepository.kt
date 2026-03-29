package com.donorconnect.api.repository

import com.donorconnect.api.model.OtpToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OtpTokenRepository : JpaRepository<OtpToken, Int> {
    // Find the token based on the user's ID
    fun findByUser_UserId(userId: Int): OtpToken?
}