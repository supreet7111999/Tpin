package com.axis.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.axis.dto.LockerApplyRequest;
import com.axis.exception.LockerAlreadyExistsException;
import com.axis.exception.LockerNotFoundException;
import com.axis.model.Locker;
import com.axis.model.OTP;
import com.axis.service.LockerService;

@RestController
@RequestMapping("/lockers")
public class LockerController {
	
	@Autowired
	private LockerService lockerService;

    @PostMapping("/createlocker")
	public ResponseEntity<?> createLocker(@RequestBody Locker locker) throws LockerAlreadyExistsException{
        lockerService.createLocker(locker);
        return new ResponseEntity<>("locker created", HttpStatus.CREATED);
    }

    
	@PostMapping("applyforlocker")
	public ResponseEntity<?> ApplyForLocker(@RequestBody LockerApplyRequest locker) throws LockerNotFoundException{
        String res = lockerService.applyForLocker(locker);
        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }
	
	@PostMapping("/rentcalc")
	public ResponseEntity<?> RentCalculation(@RequestBody LockerApplyRequest locker) throws LockerNotFoundException{
        int rent = lockerService.rentCalculation(locker);
        return new ResponseEntity<>("Total Rent is "+ rent, HttpStatus.ACCEPTED);
    }
	
	@PostMapping("/lockeravailable")
	public ResponseEntity<List<Locker>> LockerAvailabilityCheck(@RequestBody Locker locker) throws LockerNotFoundException{
        List<Locker>lockersPresent =  lockerService.lockerAvailabilityCheck(locker);
//        return new ResponseEntity<>("lockers Available: "+ !lockersPresent.isEmpty(), HttpStatus.CREATED);
        return new ResponseEntity<>(lockersPresent, HttpStatus.ACCEPTED);
    }
	
	@GetMapping("/getotp/{id}")
	public ResponseEntity<?> GenerateOTP(@PathVariable Integer id) throws LockerNotFoundException{
		Integer res = lockerService.generateOTP(id);
        return new ResponseEntity<>("Your OTP is "+ res, HttpStatus.ACCEPTED);
    }
	
	@PostMapping("/verifyotp")
	public ResponseEntity<?> VerifyOTP(@RequestBody OTP otp) throws LockerNotFoundException{
        boolean res = lockerService.verifyOTP(otp);
        return new ResponseEntity<>("Your OTP is "+ res, HttpStatus.ACCEPTED);
    }
	
}
