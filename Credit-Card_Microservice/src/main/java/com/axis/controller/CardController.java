package com.axis.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.axis.dto.ChangePinRequest;
import com.axis.dto.CreditCardApplyRequest;
import com.axis.model.Card;
import com.axis.service.CardService;

@RestController
@RequestMapping("/credit-card")
public class CardController {
	
	@Autowired
	private CardService cardService;


    @PostMapping("/applynewcard")
	public ResponseEntity<?> applyForNewCard(@RequestBody CreditCardApplyRequest creditCardReq) {
		String res = cardService.applyForNewCard(creditCardReq);
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }
        

    
    @PostMapping("/changedailylimit/{id}")
	public ResponseEntity<?> changeDailyLimit(@PathVariable Integer id, @RequestBody double dailyLimit) {
    	String res = cardService.changeDailyLimit(id, dailyLimit);
        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }
    
    @GetMapping("/activate/{id}")
	public ResponseEntity<?> activate(@PathVariable Integer id) {
        cardService.activate(id);
        return new ResponseEntity<>("Card Activated", HttpStatus.ACCEPTED);
    }
    
    @GetMapping("/deactivate/{id}")
	public ResponseEntity<?> deactivate(@PathVariable Integer id) {
        cardService.deactivate(id);
        return new ResponseEntity<>("Card Deactivated", HttpStatus.ACCEPTED);
    }
    
    @GetMapping("/findcardbyid/{id}")
	public ResponseEntity<?> findCardById(@PathVariable Integer id) {
       Card card = cardService.findCardById(id);
        return new ResponseEntity<>(card, HttpStatus.ACCEPTED);
    }
    
    @PostMapping("/findcardbyaccountid")
  	public ResponseEntity<?> findCardByAccountId(@RequestBody String accountId) {
         Card card = cardService.findCardByAccountId(accountId);
          return new ResponseEntity<>(card , HttpStatus.ACCEPTED);
      }
      
    @PostMapping("/changepin/{id}")
  	public ResponseEntity<?> changePin(@PathVariable Integer id, @RequestBody ChangePinRequest changePin) {
      	String res = cardService.changePin(id, changePin.getNewPin(), changePin.getPin());
          return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
      }
    
    
}