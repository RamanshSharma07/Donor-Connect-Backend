package com.donorconnect.api.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "requests")
class Request(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "requestid")
    val requestId: Int = 0,

    // A recipient can make multiple blood requests
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipientid", nullable = false)
    var recipient: Recipient,

    @Column(name = "bloodgroup", nullable = false, length = 5)
    var bloodGroup: String,

    @Column(name = "urgencylevel", nullable = false, length = 20)
    var urgencyLevel: String,

    @Column(length = 20)
    var status: String = "Pending",

    @Column(name = "requestdate", nullable = false)
    var requestDate: LocalDateTime = LocalDateTime.now()

) : BaseAuditableEntity()