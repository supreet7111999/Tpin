package com.banking.emailmodel;

import jakarta.annotation.Generated;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "EmailStatusTable")
@NoArgsConstructor
@AllArgsConstructor
public class EmailPojo {
	
	@Id 
	@GeneratedValue(strategy = GenerationType.AUTO)
	
	private int id;
	private String reciever;
	private String subject;
	private String message;
	@Enumerated(EnumType.STRING)
	private Status status;
	

}
