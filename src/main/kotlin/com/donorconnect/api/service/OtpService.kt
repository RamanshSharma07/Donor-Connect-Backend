package com.donorconnect.api.service

import com.donorconnect.api.model.OtpToken
import com.donorconnect.api.model.User
import com.donorconnect.api.repository.OtpTokenRepository
import com.donorconnect.api.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import kotlin.random.Random

@Service
class OtpService(
    private val otpTokenRepository: OtpTokenRepository,
    private val emailService: EmailService,
    private val userRepository: UserRepository
) {

    @Transactional
    fun generateAndSendOtp(user: User) {
        // 1. Generate a random 6-digit string
        val otpCode = Random.nextInt(100000, 999999).toString()

        // 2. Check if this user already has an OTP in the database (e.g., they clicked "Resend")
        val existingToken = otpTokenRepository.findByUser_UserId(user.userId)

        if (existingToken != null) {
            // Update the existing token with the new code and a fresh 15-minute timer
            existingToken.otpCode = otpCode
            existingToken.expirationTime = LocalDateTime.now().plusMinutes(15)
            otpTokenRepository.save(existingToken)
        } else {
            // Create a brand-new token
            val newToken = OtpToken(
                otpCode = otpCode,
                expirationTime = LocalDateTime.now().plusMinutes(15),
                user = user
            )
            otpTokenRepository.save(newToken)
        }

        // 3. Fire off the email!
        emailService.sendOtpEmail(user.email, otpCode)
    }


    @Transactional
    fun verifyOtp(email: String, otpCode: String): Boolean {
        // 1. Find the user
        val user = userRepository.findByEmail(email)
            ?: throw IllegalArgumentException("User not found.")

        // 2. If they are already verified, just return true
        if (user.isVerified) {
            return true
        }

        // 3. Find their active OTP token
        val token = otpTokenRepository.findByUser_UserId(user.userId)
            ?: throw IllegalArgumentException("No active OTP found for this user.")

        // 4. Check if it's expired
        if (token.isExpired()) {
            throw IllegalArgumentException("OTP has expired. Please request a new one.")
        }

        // 5. Check if the code matches exactly
        if (token.otpCode != otpCode) {
            throw IllegalArgumentException("Invalid OTP code.")
        }

        // 6. Success! Mark the user as verified
        user.isVerified = true
        userRepository.save(user)

        // 7. Delete the token so it can't be used again
        otpTokenRepository.delete(token)

        return true
    }
}