package com.axis.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.axis.model.OTP;

public interface OtpRepository extends JpaRepository<OTP, Integer> {

}
