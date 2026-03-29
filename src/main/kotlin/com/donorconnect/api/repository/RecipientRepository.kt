package com.donorconnect.api.repository

import com.donorconnect.api.model.Recipient
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RecipientRepository : JpaRepository<Recipient, Int> {
    fun findByUser_UserId(userId: Int): Recipient?
}