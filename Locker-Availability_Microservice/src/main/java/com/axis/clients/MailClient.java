package com.axis.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.axis.dto.EmailRequest;

@FeignClient(name="MAIL-SERVICE")
public interface MailClient {
	
	@RequestMapping(value="/sendemail",method=RequestMethod.POST)
	public boolean sendEmail(@RequestBody EmailRequest request);

}
