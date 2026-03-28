package com.donorconnect.api.v1.database.repository

import com.donorconnect.api.model.Request
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RequestRepository : JpaRepository<Request, Int> {
    fun findByRecipient_RecipientId(recipientId: Int): List<Request>
    fun findByStatus(status: String): List<Request>
}