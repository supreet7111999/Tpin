package com.axis.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="locker")
public class Locker {

	@Id
	@GeneratedValue
    private Integer id;
	
	@Enumerated(EnumType.STRING
			
			)
    private Size size;
   
	private boolean isAvailable;
	
	private Date creationDate;
    
	private Date allocatedDate;
    
	//	private Date expirationDate;  
	
	//implement annual allocation only
    private int durationForAllocatingInYears;
	
	private String branchName;
	
//    todo: add list<locker> in user 
	
	
}

