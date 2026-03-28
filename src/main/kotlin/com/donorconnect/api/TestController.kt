package com.donorconnect.api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController {

    @GetMapping("/api/test")
    fun testEndpoint(): String {
        return "DonorConnect Backend is successfully running!"
    }
}