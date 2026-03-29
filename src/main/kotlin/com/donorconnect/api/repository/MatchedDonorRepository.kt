package com.donorconnect.api.repository

import com.donorconnect.api.model.MatchedDonor
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MatchedDonorRepository : JpaRepository<MatchedDonor, Int> {
    fun findByRequest_RequestId(requestId: Int): List<MatchedDonor>
}