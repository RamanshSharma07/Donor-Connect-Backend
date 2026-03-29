package com.donorconnect.api.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.Date
import javax.crypto.SecretKey

@Component
class JwtUtil(
    @Value("\${jwt.secret}") private val secretString: String,
    @Value("\${jwt.expiration}") private val expirationMs: Long
) {

    // Converts your string secret into a cryptographic key
    private val secretKey: SecretKey by lazy {
        Keys.hmacShaKeyFor(secretString.toByteArray())
    }

    /**
     * Generates a new JWT when a user successfully logs in.
     */
    fun generateToken(email: String, role: String): String {
        return Jwts.builder()
            .subject(email) // The core identity of the user
            .claim("role", role) // Adding custom data (claims)
            .issuedAt(Date())
            .expiration(Date(System.currentTimeMillis() + expirationMs))
            .signWith(secretKey) // Signs it so it cannot be tampered with
            .compact()
    }

    /**
     * Extracts the user's email from a token.
     */
    fun extractEmail(token: String): String {
        return Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .payload
            .subject
    }

    /**
     * Checks if the token is mathematically valid and not expired.
     */
    fun isTokenValid(token: String): Boolean {
        return try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token)
            true
        } catch (e: Exception) {
            // If it's expired, tampered with, or malformed, it throws an exception
            false
        }
    }
}