package com.axis.model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="card")
public class Card {
	
	  @Id
	  @GeneratedValue
	  private Integer id;
  
	  private String accountId;
	 
    private String cardNumber;
    private String holderName;
    private CardType cardType; // 1: VISA Card, 2: Master Card
   
    private Date allotmentDate;
    private Date expiredDate;
    private String csv;
    
    private Double dailyLimit;
    private Boolean status;
    
    private Integer pin;
  }
