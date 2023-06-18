package com.sr.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sr.entity.GiftCard;
import com.sr.model.AvailGiftRequest;
import com.sr.model.BuyGiftRequest;
import com.sr.service.GiftService;

@RestController
@RequestMapping("/gift")
public class GiftCardController {
  @Autowired
  private GiftService giftService;
  @PostMapping("/buyGiftCard/{userId}")
  public String buyGiftCard(@PathVariable("userId") long userId,@RequestBody BuyGiftRequest bgr)
  {
	  return giftService.buyGiftCard(userId, bgr.getAmt());
  }
  @GetMapping("/getAllGifts")
  public List<GiftCard> getAllGifts(){
	  return giftService.getAllGifts();
  }
  @GetMapping("/getYourGifts/{userId}")
  public List<GiftCard> getYourGifts(@PathVariable("userId") long userId){
	  return giftService.getYourGifts(userId);
  }
  @PostMapping("/availGiftCard/{userId}")
  public String availGiftCard(@PathVariable("userId") long userId,@RequestBody AvailGiftRequest agr) {
	return giftService.availGiftCard(agr.getGiftId(),agr.getToken(), userId);  
  }
}
