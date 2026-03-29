package com.donorconnect.api.v1.controller

import com.donorconnect.api.service.UserService
import com.donorconnect.api.v1.dto.UpdateProfileRequest
import com.donorconnect.api.v1.dto.UserProfileResponse
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userService: UserService
) {

    @GetMapping("/me")
    fun getCurrentUserProfile(authentication: Authentication): ResponseEntity<UserProfileResponse> {
        // Because of our JwtAuthenticationFilter, Spring Security already knows who this is!
        // authentication.name automatically holds the email we extracted from the token.
        val email = authentication.name

        val profile = userService.getUserProfile(email)

        return ResponseEntity.ok(profile)
    }

    @PutMapping("/me")
    fun updateProfile(
        authentication: Authentication,
        @RequestBody request: UpdateProfileRequest
    ): ResponseEntity<Any> {
        return try {
            // authentication.name holds the email we extracted from their JWT
            val email = authentication.name

            userService.updateUserProfile(email, request)

            ResponseEntity.ok(
                mapOf("message" to "Profile updated successfully.")
            )
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(404).body(
                mapOf("error" to e.message)
            )
        } catch (e: Exception) {
            ResponseEntity.status(500).body(
                mapOf("error" to "An unexpected error occurred while updating the profile.")
            )
        }
    }
}