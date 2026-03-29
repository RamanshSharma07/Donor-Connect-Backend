package com.donorconnect.api.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtUtil: JwtUtil
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        // 1. Look for the "Authorization" header in the incoming request
        val authHeader = request.getHeader("Authorization")

        // 2. If there is no header, or it doesn't start with "Bearer ", pass it down the chain.
        // (Spring Security will eventually block it if the endpoint requires auth).
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response)
            return
        }

        // 3. Extract the actual token string (removing the "Bearer " prefix)
        val token = authHeader.substring(7)

        // 4. Validate the token using our JwtUtil
        if (jwtUtil.isTokenValid(token)) {
            val email = jwtUtil.extractEmail(token)

            // 5. Create an Authentication object for Spring Security
            // (We assign a default ROLE_USER authority here to keep it simple)
            val authentication = UsernamePasswordAuthenticationToken(
                email,
                null,
                listOf(SimpleGrantedAuthority("ROLE_USER"))
            )

            // Log the network details (like IP address)
            authentication.details = WebAuthenticationDetailsSource().buildDetails(request)

            // 6. Tell Spring Security: "This user is officially logged in for this request!"
            SecurityContextHolder.getContext().authentication = authentication
        }

        // 7. Continue processing the request
        filterChain.doFilter(request, response)
    }
}