package com.banking.emailController;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.banking.emailService.EmailService;
import com.banking.emailmodel.EmailRequest;

import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
public class EmailSenderController {
	
	@Autowired
	private  EmailService emailService;
	private Logger logger= LoggerFactory.getLogger(EmailSenderController.class);
	
	@PostMapping("/sendemail")
//	public String sendMail(@RequestParam(value = "file" , required = false )MultipartFile[] file ,String to , String subject ,String body ) {
	public String sendMail(@RequestBody EmailRequest EmailRequest ) {
//		String to = emailPojo.getReciever();
//		String subject =emailPojo.getSubject();
//		String body = emailPojo.getMessage();
//		logger.info(to);
//		return emailService.sendMail(file ,to , subject ,body);
		return emailService.sendMail(EmailRequest);

	}
	

}
