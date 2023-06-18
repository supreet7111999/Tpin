package com.banking.emailService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.banking.emailController.EmailSenderController;
import com.banking.emailmodel.EmailPojo;
import com.banking.emailmodel.EmailRequest;
import com.banking.emailmodel.Status;
import com.banking.emailrepository.EmailRepository;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
	
	@Value("${spring.mail.username}")
	private String fromEmail;
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	@Autowired
	private EmailRepository emailRepository;
	private Logger logger= LoggerFactory.getLogger(EmailSenderController.class);

//	public String sendMail(MultipartFile[] file, String to,  String subject, String body) {
		public String sendMail( EmailRequest EmailRequest ) {
			EmailPojo ep = new EmailPojo();
			ep.setReciever(EmailRequest.getTo());
			ep.setMessage(EmailRequest.getMessage().substring(0,30));
			ep.setSubject(EmailRequest.getSubject());
			
			
			 emailRepository.save(ep);
			  String cc [] = {
//					  "lokeshreddu123@gmail.com",
					  "supreet7111999@gmail.com",
//					 " veenuj3112@gmail.com",
//					 "sanskarsingh8176@gmail.com"
					  
			  };
		
			 try {
					
					
					MimeMessage mimeMessage = javaMailSender.createMimeMessage();
					
					MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage ,true );
					
					messageHelper.setFrom(fromEmail);	
					messageHelper.setTo(ep.getReciever());
					messageHelper.setCc(cc);
					messageHelper.setSubject(ep.getSubject());
					messageHelper.setText(ep.getMessage());
					
//					for(int i=0 ; i< file.length ; i++) {
//						messageHelper.addAttachment(
//								file[i].getOriginalFilename(),
//								new ByteArrayResource(file[i].getBytes())
//								);
//					}
//					
					javaMailSender.send(mimeMessage);;
					ep.setStatus(Status.SENT);
					emailRepository.save(ep);
					
					return "Mail sent to user";
					
				} catch (Exception e) {
					// TODO: handle exception
					throw new RuntimeException();
				}
			}

}
