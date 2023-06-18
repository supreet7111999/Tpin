package com.sr.service;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sr.client.TransactionClient;
import com.sr.client.AccountClient;
import com.sr.client.UserClient;
import com.sr.entity.GiftCard;
import com.sr.repo.GiftCardRepo;
import com.sr.dto.*;
@Service
public class GiftService {
   @Autowired
   private GiftCardRepo repo;
   @Autowired
   private UserClient userClient;
   @Autowired
   private AccountClient accountClient;
   @Autowired
   private TransactionClient transactionClient;
   
   public String buyGiftCard(long userId,long amt) {
	   UserDTO user=userClient.getUser(userId);
	   if(user==null)
		   return "You are not registered";
	   int min=100000;
	   int max=999999;
	   GiftCard giftCard=new GiftCard();
	   giftCard.setAmt(amt);
	   giftCard.setExpired(false);
//	   Date d=new Date();
	   
	   Calendar cal = Calendar.getInstance();
	   Date today = cal.getTime();
	   cal.add(Calendar.YEAR, 1); // to get previous year add -1
	   Date nextYear = cal.getTime();
	   
	   giftCard.setIssueDate(today);
	   giftCard.setExpiryDate(nextYear);
	   giftCard.setUserId(userId);
	   
	   int token=(int) (Math.random() * (max - min + 1) + min);
	 
	   giftCard.setToken(token);
	   String s=Integer.toString(token);
	   
	   AccountDTO account=accountClient.getCustomerAccounts(userId);
	   if(account==null) {
		return "Account not found with userid";
	   }
	   DeductGiftCardCharges deductGiftCardCharges=new DeductGiftCardCharges(account.getAccountId(),amt);
	   transactionClient.deductGiftCardCharges(deductGiftCardCharges);
	   repo.save(giftCard);
	   return s;
   }
  
  public List<GiftCard> getAllGifts()
  {
	  return repo.findAll();
  }
  public List<GiftCard> getYourGifts(long userId){
	  return repo.findAllByuserId(userId);
  }
  
  public String availGiftCard(int giftId,int token,long userId)
  {
	  UserDTO user=userClient.getUser(userId);
	  if(user==null)
		   return "You are not registered";
	  AccountDTO account=accountClient.getCustomerAccounts(userId);
	  if(account==null)
		  return "No bank Account Linked";
	  var giftCard=repo.findById(giftId).get();
	  if(giftCard==null)
		  return "Wrong Details";
	  if(giftCard.getToken()!=token)
		  return "Wrong Details";
	  if(giftCard.isExpired()==true)
	     return "Gift Card Experied";
	  
	  giftCard.setUsedBy(userId);
	  giftCard.setExpired(true);
	  //logic for adding money in the userId's account
	  AvailGiftCard availGiftCard=new AvailGiftCard(account.getAccountId(),giftCard.getAmt());
	  transactionClient.availGiftCard(availGiftCard);
	  repo.save(giftCard);
	  return "GiftCard Redeemed";
  }
}
