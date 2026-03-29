package com.donorconnect.api.v1.controller

import com.donorconnect.api.service.OtpService
import com.donorconnect.api.service.UserService
import com.donorconnect.api.v1.dto.ForgotPasswordRequest
import com.donorconnect.api.v1.dto.LoginRequest
import com.donorconnect.api.v1.dto.ResetPasswordRequest
import com.donorconnect.api.v1.dto.UserRegistrationRequest
import com.donorconnect.api.v1.dto.VerifyOtpRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val userService: UserService,
    private val otpService: OtpService
) {

    @PostMapping("/register")
    fun register(@RequestBody request: UserRegistrationRequest): ResponseEntity<Any> {
        return try {
            // Hand the heavy lifting to the Service layer
            userService.registerUser(request)

            // Return a clean 201 Created response with a JSON success message
            ResponseEntity.status(HttpStatus.CREATED).body(
                mapOf("message" to "User registered successfully!")
            )
        } catch (e: IllegalArgumentException) {
            // If the user submits a duplicate email, return a 400 Bad Request
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                mapOf("error" to e.message)
            )
        } catch (e: Exception) {
            // Catch-all for any other server errors
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                mapOf("error" to "An unexpected error occurred during registration.")
            )
        }
    }

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<Any> {
        return try {
            val response = userService.loginUser(request)
            ResponseEntity.ok(response) // Returns an HTTP 200 OK with the token payload
        } catch (e: IllegalArgumentException) {
            // Returns a 401 Unauthorized if the email/password is wrong
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                mapOf("error" to e.message)
            )
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                mapOf("error" to "An unexpected error occurred during login.")
            )
        }
    }

    @PostMapping("/verify-email")
    fun verifyEmail(@RequestBody request: VerifyOtpRequest): ResponseEntity<Any> {
        return try {
            otpService.verifyOtp(request.email, request.otpCode)

            ResponseEntity.ok(
                mapOf("message" to "Email successfully verified!")
            )
        } catch (e: IllegalArgumentException) {
            // Return a 400 Bad Request if the OTP is wrong or expired
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                mapOf("error" to e.message)
            )
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                mapOf("error" to "An unexpected error occurred during verification.")
            )
        }
    }

    @PostMapping("/forgot-password")
    fun forgotPassword(@RequestBody request: ForgotPasswordRequest): ResponseEntity<Any> {
        return try {
            userService.forgotPassword(request)
            // 1. If we get here, the email exists. Android can redirect to the OTP screen!
            ResponseEntity.ok(
                mapOf("message" to "An OTP has been sent to your email.")
            )
        } catch (e: IllegalArgumentException) {
            // 2. If the email doesn't exist, tell Android so it can show the "Register" button!
            // Using 404 NOT FOUND is the standard HTTP status for this scenario.
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                mapOf("error" to "Email not registered. Please create an account.")
            )
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                mapOf("error" to "An unexpected error occurred.")
            )
        }
    }

    @PostMapping("/reset-password")
    fun resetPassword(@RequestBody request: ResetPasswordRequest): ResponseEntity<Any> {
        return try {
            userService.resetPassword(request)
            ResponseEntity.ok(mapOf("message" to "Password successfully reset! You can now log in."))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("error" to e.message))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                mapOf("error" to "An unexpected error occurred.")
            )
        }
    }
}