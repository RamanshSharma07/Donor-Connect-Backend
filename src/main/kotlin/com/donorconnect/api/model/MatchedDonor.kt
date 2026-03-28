package com.donorconnect.api.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "matched_donor")
class MatchedDonor(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "matchid")
    val matchId: Int = 0,

    // Links to the specific blood request
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requestid", nullable = false)
    var request: Request,

    // Links to the donor who accepted or was matched
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donorid", nullable = false)
    var donor: Donor,

    @Column(name = "matchdate", nullable = false)
    var matchDate: LocalDateTime = LocalDateTime.now()

) : BaseAuditableEntity()