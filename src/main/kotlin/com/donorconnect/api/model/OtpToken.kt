package com.donorconnect.api.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "otp_tokens")
class OtpToken(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "otpid")
    val otpId: Int = 0,

    @Column(name = "otp_code", nullable = false, length = 6)
    var otpCode: String,

    @Column(name = "expiration_time", nullable = false)
    var expirationTime: LocalDateTime,

    // One user can have one active OTP at a time
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid", nullable = false)
    var user: User
) : BaseAuditableEntity() {

    // Helper function to check if the OTP is expired
    fun isExpired(): Boolean {
        return LocalDateTime.now().isAfter(expirationTime)
    }
}