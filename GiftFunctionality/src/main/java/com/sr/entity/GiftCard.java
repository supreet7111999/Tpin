package com.sr.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="GiftCard")
public class GiftCard {
	@Id
	@GeneratedValue
   private int giftId;
   private long amt;
   private Date issueDate;
   private Date expiryDate;
   private int token;
   private long userId;
   private long usedBy;
   private boolean isExpired;
}
