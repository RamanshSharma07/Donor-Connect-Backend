package com.donorconnect.api.model

import jakarta.persistence.*

@Entity
@Table(name = "users")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userid")
    val userId: Int = 0,

    @Column(nullable = false, length = 100)
    var name: String,

    @Column(unique = true, nullable = false, length = 100)
    var email: String,

    @Column(nullable = false, length = 200)
    var password: String,

    @Column(length = 20)
    var role: String = "user",

    @Column(nullable = false, length = 20)
    var contact: String,

    @Column(nullable = false, length = 200)
    var location: String
): BaseAuditableEntity()