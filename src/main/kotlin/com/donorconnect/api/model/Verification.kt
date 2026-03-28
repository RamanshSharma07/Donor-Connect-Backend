package com.donorconnect.api.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "verification")
class Verification(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "verifyid")
    val verifyId: Int = 0,

    // The hospital that performed the verification
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospitalid", nullable = false)
    var hospital: Hospital,

    // The donor being verified
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donorid", nullable = false)
    var donor: Donor,

    @Column(name = "verifiedon", nullable = false)
    var verifiedOn: LocalDateTime = LocalDateTime.now()

) : BaseAuditableEntity()