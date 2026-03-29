package com.donorconnect.api.model

import jakarta.persistence.*

@Entity
@Table(name = "recipients")
class Recipient(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recipientid")
    val recipientId: Int = 0,

    // Links directly back to the main User profile
    @OneToOne
    @JoinColumn(name = "userid", nullable = false)
    var user: User,

    @Column(name = "bloodgroup", nullable = false, length = 5)
    var bloodGroup: String
): BaseAuditableEntity()

