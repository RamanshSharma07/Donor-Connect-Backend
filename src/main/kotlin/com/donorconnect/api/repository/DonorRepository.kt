package com.donorconnect.api.repository

import com.donorconnect.api.model.Donor
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface DonorRepository : JpaRepository<Donor, Int> {

    fun findByUser_UserId(userId: Int): Donor?

    // Updated Query: Removed the bloodGroup requirement!
    // Now it just finds anyone who is manually available and medically eligible.
    @Query("""
        SELECT d FROM Donor d 
        WHERE d.isAvailable = true 
        AND (d.lastDonationDate IS NULL OR d.lastDonationDate <= :eligibleDate)
    """)
    fun findAvailableAndEligibleDonors(
        @Param("eligibleDate") eligibleDate: LocalDate
    ): List<Donor>
}