package com.donorconnect.api.v1.controller

import com.donorconnect.api.service.UserService
import com.donorconnect.api.v1.dto.UserRegistrationRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val userService: UserService
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
}