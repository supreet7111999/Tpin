package com.axis.dto;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@SuppressWarnings("unused")
//@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

//@Entity
//@Table(name = "_user")
public class User {
	
	  @Id
	  private Integer id;
	  private String firstname;
	  private String lastname;
	  private String email;
	  

//	  @OneToOne(mappedBy = "userId")
//	  private Card card;
	  
	  
}