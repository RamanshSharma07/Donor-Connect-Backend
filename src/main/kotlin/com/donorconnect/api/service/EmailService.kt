package com.donorconnect.api.service

import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class EmailService(
    private val mailSender: JavaMailSender
) {

    fun sendOtpEmail(toEmail: String, otpCode: String) {
        val message = SimpleMailMessage()
        message.from = "noreply@donorconnect.com" // This is just a display name; Gmail still sends it from your real address
        message.setTo(toEmail)
        message.subject = "OTP to Verify Your DonorConnect Account"
        message.text = """
            Welcome to DonorConnect!
            
            Your email verification code is: $otpCode
            
            This code will expire in 15 minutes. If you did not request this, please ignore this email.
        """.trimIndent()

        mailSender.send(message)
    }

    fun sendPasswordResetEmail(toEmail: String, otpCode: String) {
        val message = SimpleMailMessage()
        message.from = "noreply@donorconnect.com"
        message.setTo(toEmail)
        message.subject = "OTP to Reset Your DonorConnect Password"
        message.text = """
            We received a request to reset your password.
            
            Your password reset code is: $otpCode
            
            This code will expire in 15 minutes. If you did not request a password reset, please ignore this email.
        """.trimIndent()

        mailSender.send(message)
    }
}