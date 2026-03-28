package com.donorconnect.api.v1.database.repository

import com.donorconnect.api.model.Hospital
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface HospitalRepository : JpaRepository<Hospital, Int>