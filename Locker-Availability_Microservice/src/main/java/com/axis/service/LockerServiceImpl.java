package com.axis.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.axis.clients.MailClient;
import com.axis.clients.TransactionClient;
import com.axis.clients.UserClient;
import com.axis.dto.DeductLockerChargesRequest;
import com.axis.dto.LockerApplyRequest;
import com.axis.exception.LockerNotFoundException;
import com.axis.model.Locker;
import com.axis.model.OTP;
import com.axis.model.Size;
import com.axis.repository.LockerRepository;
import com.axis.repository.OtpRepository;

@Service
public class LockerServiceImpl implements LockerService{

	@Autowired
	private  LockerRepository lockerRepository;
	@Autowired
	private  OtpRepository otpRepository;

	@Autowired
	private MailClient mailClient;
	
	@Autowired
	private UserClient userClient;
	
	@Autowired
	private TransactionClient transactionClient;
	
	
	@Override
	public Locker createLocker(Locker locker) {
		locker.setCreationDate(new Date());
		locker.setAvailable(true);
		locker.setDurationForAllocatingInYears(0);
		System.out.println(locker);
		Locker res = lockerRepository.save(locker);
		
		return res;
	}
	
	
	@Override
	public String applyForLocker(LockerApplyRequest locker) throws LockerNotFoundException{		
		int rent = rentCalculation(locker);

		
		Optional<Locker> Lockers = lockerRepository.findById(locker.getId());
		if(!Lockers.isPresent()) {
			throw new LockerNotFoundException("locker not found with id "+ locker.getId());
		}
		Locker updateLocker = Lockers.get();
		
		updateLocker.setAvailable(false);
		updateLocker.setAllocatedDate(new Date());
		updateLocker.setDurationForAllocatingInYears(locker.getDurationForAllocatingInYears());
		
		transactionClient.lockerCharge(new DeductLockerChargesRequest(locker.getFromAccountId(), rent));			

		return "Applied for Locker at "+ rent + " Rupees rent for "+ locker.getDurationForAllocatingInYears()+" years";

	}

	@Override
	public List<Locker> lockerAvailabilityCheck(Locker locker) throws LockerNotFoundException {
		List<Locker> lockerPresent = lockerRepository.findLockerBySizeAndBranchNameAndIsAvailable(locker.getSize(),locker.getBranchName(),true);
//		System.out.println(lockerPresent);
		
		if(lockerPresent.isEmpty()) {
			throw new LockerNotFoundException("No Locker of Your choice, kindly update your search");
		}
		
		return lockerPresent;
	}

	@Override
	public int rentCalculation(LockerApplyRequest locker) {
		Size size = locker.getSize();
		int rentof1yr = size.getNumVal();
		int netRate = rentof1yr * locker.getDurationForAllocatingInYears();
		return netRate;
	}


	@Override
	public Integer generateOTP(Integer id) {
		
		Optional<Locker> locker = lockerRepository.findById(id);
		
		if(locker.isEmpty()) {
			throw new LockerNotFoundException("locker not found with id "+ id);
		}
		
		Integer otp = (int) Math.floor(1000 + Math.random() * 9000);
		OTP otpgenerated = new OTP();
		otpgenerated.setLockerid(id);
		otpgenerated.setOtp(otp);
		otpRepository.save(otpgenerated);
		
		System.out.println(otp);
		return otp;
	}

	@Override
	public boolean verifyOTP(OTP otp) {
		Optional<OTP> otpsaved = otpRepository.findById(otp.getLockerid());
		double otpinsystem = otpsaved.get().getOtp();
		
		return (otpinsystem == otp.getOtp());
	}




}
