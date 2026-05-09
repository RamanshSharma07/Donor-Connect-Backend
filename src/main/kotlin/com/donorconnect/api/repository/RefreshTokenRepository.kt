package com.donorconnect.api.repository

import com.donorconnect.api.model.RefreshToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RefreshTokenRepository : JpaRepository<RefreshToken, Int> {
    fun findByToken(token: String): RefreshToken?
    fun findByUser_UserId(userId: Int): RefreshToken?
}