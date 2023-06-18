package com.axis.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="otp")
public class OTP {
	
	@Id
	private Integer lockerid;
	private Integer otp;
	
}

