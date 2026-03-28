package com.donorconnect.api.model

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "donors")
class Donor(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "donorid")
    val donorId: Int = 0,

    // This is how Spring handles the Foreign Key connection
    @OneToOne
    @JoinColumn(name = "userid", nullable = false)
    var user: User,

    @Column(name = "bloodgroup", nullable = false, length = 5)
    var bloodGroup: String,

    @Column(nullable = false)
    var age: Int,

    @Column(nullable = false, length = 10)
    var gender: String,

    @Column(nullable = false)
    var verified: Boolean = false,
    @Column(name = "isavailable", nullable = false)
    var isAvailable: Boolean = true, // The manual switch

    @Column(name = "lastdonationdate")
    var lastDonationDate: LocalDate? = null // Null if they've never donated

): BaseAuditableEntity()