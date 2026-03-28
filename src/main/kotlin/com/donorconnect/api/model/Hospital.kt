package com.donorconnect.api.model

import jakarta.persistence.*

@Entity
@Table(name = "hospitals")
class Hospital(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hospitalid")
    val hospitalId: Int = 0,

    @Column(nullable = false, length = 200)
    var name: String,

    @Column(nullable = false, length = 200)
    var location: String,

    @Column(nullable = false, length = 20)
    var contact: String
): BaseAuditableEntity()