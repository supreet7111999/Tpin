package com.axis.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.axis.dto.LockerApplyRequest;
import com.axis.exception.LockerNotFoundException;
import com.axis.model.Locker;
import com.axis.model.OTP;

@Service
public interface LockerService {

	public Locker createLocker(Locker locker);
	
	public List<Locker> lockerAvailabilityCheck(Locker locker) throws LockerNotFoundException;
	
	public String applyForLocker(LockerApplyRequest locker)throws LockerNotFoundException;
	public int rentCalculation(LockerApplyRequest locker);
	
	public Integer generateOTP(Integer id);
	public boolean verifyOTP(OTP otp);
	
}
