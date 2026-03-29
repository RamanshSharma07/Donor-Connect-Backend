package com.donorconnect.api.service


import com.donorconnect.api.model.Donor
import com.donorconnect.api.model.Recipient
import com.donorconnect.api.model.User
import com.donorconnect.api.repository.DonorRepository
import com.donorconnect.api.repository.RecipientRepository
import com.donorconnect.api.repository.UserRepository
import com.donorconnect.api.security.JwtUtil
import com.donorconnect.api.v1.dto.ForgotPasswordRequest
import com.donorconnect.api.v1.dto.LoginRequest
import com.donorconnect.api.v1.dto.LoginResponse
import com.donorconnect.api.v1.dto.ResetPasswordRequest
import com.donorconnect.api.v1.dto.UserProfileResponse
import com.donorconnect.api.v1.dto.UserRegistrationRequest
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val donorRepository: DonorRepository,
    private val recipientRepository: RecipientRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil,
    private val otpService: OtpService
) {

    // @Transactional ensures that if saving the Donor/Recipient fails,
    // the User save is rolled back. No orphaned data!
    @Transactional
    fun registerUser(request: UserRegistrationRequest): User {

        // 1. Business Rule: Prevent duplicate emails
        if (userRepository.existsByEmail(request.email)) {
            throw IllegalArgumentException("Email is already registered.")
        }

        // 2. Hash the password before saving
        val hashedPassword = passwordEncoder.encode(request.password).toString()

        // 3. Create and save the main User profile
        val newUser = User(
            name = request.name,
            email = request.email,
            password = hashedPassword,
            contact = request.contact,
            location = request.location,
            role = "user" // Default role
        )
        val savedUser = userRepository.save(newUser)

        // 4. Create the specific sub-profile based on their choice
        if (request.isDonor) {
            requireNotNull(request.bloodGroup) { "Blood group is required for donors." }
            requireNotNull(request.age) { "Age is required for donors." }
            requireNotNull(request.gender) { "Gender is required for donors." }

            val newDonor = Donor(
                user = savedUser,
                bloodGroup = request.bloodGroup,
                age = request.age,
                gender = request.gender,
                isAvailable = true
            )
            donorRepository.save(newDonor)
        } else {
            requireNotNull(request.bloodGroup) { "Blood group is required for recipients." }

            val newRecipient = Recipient(
                user = savedUser,
                bloodGroup = request.bloodGroup
            )
            recipientRepository.save(newRecipient)
        }

        // Generate and send the verification email in the background
        otpService.generateAndSendOtp(savedUser)

        return savedUser
    }

    fun loginUser(request: LoginRequest): LoginResponse {
        // 1. Find the user by email
        val user = userRepository.findByEmail(request.email)
            ?: throw IllegalArgumentException("Invalid email or password.") // Vague error is a security best practice!

        // 2. Check if the password matches the hashed password in the database
        if (!passwordEncoder.matches(request.password, user.password)) {
            throw IllegalArgumentException("Invalid email or password.")
        }

        // 3. Generate the JWT
        val token = jwtUtil.generateToken(user.email, user.role)

        // 4. Return the token and basic user info to the Android app
        return LoginResponse(
            token = token,
            userId = user.userId,
            name = user.name,
            role = user.role
        )
    }

    fun getUserProfile(email: String): UserProfileResponse {
        // 1. Fetch the core User details
        val user = userRepository.findByEmail(email)
            ?: throw IllegalArgumentException("User not found.")

        // 2. Check if they have a Donor profile
        val donor = donorRepository.findByUser_UserId(user.userId)

        if (donor != null) {
            // They are a Donor! Return all their donor-specific fields.
            return UserProfileResponse(
                userId = user.userId,
                name = user.name,
                email = user.email,
                contact = user.contact,
                location = user.location,
                role = user.role,
                isDonor = true,
                bloodGroup = donor.bloodGroup,
                age = donor.age,
                gender = donor.gender,
                isAvailable = donor.isAvailable,
                lastDonationDate = donor.lastDonationDate
            )
        } else {
            // 3. Not a donor? Fetch their Recipient profile instead.
            val recipient = recipientRepository.findByUser_UserId(user.userId)

            return UserProfileResponse(
                userId = user.userId,
                name = user.name,
                email = user.email,
                contact = user.contact,
                location = user.location,
                role = user.role,
                isDonor = false,
                bloodGroup = recipient?.bloodGroup // Safe call in case data is missing
            )
        }
    }

    fun forgotPassword(request: ForgotPasswordRequest) {
        val user = userRepository.findByEmail(request.email)
            ?: throw IllegalArgumentException("Email not registered.")

        otpService.generateAndSendPasswordResetOtp(user)
    }

    @Transactional
    fun resetPassword(request: ResetPasswordRequest) {
        // 1. Verify the OTP (this will throw an error if it's invalid or expired)
        otpService.verifyOtp(request.email, request.otpCode)

        // 2. Find the user
        val user = userRepository.findByEmail(request.email)
            ?: throw IllegalArgumentException("User not found.")

        // 3. Hash the new password and save it
        user.password = passwordEncoder.encode(request.newPassword).toString()
        userRepository.save(user)
    }
}