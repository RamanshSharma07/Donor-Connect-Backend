package com.donorconnect.api.service

import com.donorconnect.api.model.RefreshToken
import com.donorconnect.api.repository.RefreshTokenRepository
import com.donorconnect.api.repository.UserRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.UUID

@Service
class RefreshTokenService(
    private val refreshTokenRepository: RefreshTokenRepository,
    private val userRepository: UserRepository,
    @Value("\${jwt.refreshExpiration}") private val refreshExpirationMs: Long
) {

    @Transactional
    fun createRefreshToken(userId: Int): RefreshToken {
        val user = userRepository.findById(userId).orElseThrow {
            IllegalArgumentException("User not found")
        }

        // Check if they already have a token (e.g., they logged in previously)
        var refreshToken = refreshTokenRepository.findByUser_UserId(userId)

        if (refreshToken != null) {
            // Overwrite the old token with a fresh one
            refreshToken.token = UUID.randomUUID().toString()
            refreshToken.expiryDate = Instant.now().plusMillis(refreshExpirationMs)
        } else {
            // Create a brand new token
            refreshToken = RefreshToken(
                user = user,
                token = UUID.randomUUID().toString(),
                expiryDate = Instant.now().plusMillis(refreshExpirationMs)
            )
        }

        return refreshTokenRepository.save(refreshToken)
    }

    fun verifyExpiration(token: RefreshToken): RefreshToken {
        if (token.expiryDate < Instant.now()) {
            refreshTokenRepository.delete(token)
            throw IllegalArgumentException("Refresh token was expired. Please make a new signin request")
        }
        return token
    }
}