package com.donorconnect.api.repository


import com.donorconnect.api.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Int> {

    // Spring Boot automatically writes the SQL for this just by reading the function name!
    fun findByEmail(email: String): User?

    fun existsByEmail(email: String): Boolean
}