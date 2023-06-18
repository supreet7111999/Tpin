package com.axis.service;

import java.util.List;

import com.axis.dto.CreditCardApplyRequest;
import com.axis.exception.CreditCardAlreadyExistsException;
import com.axis.exception.CreditCardNotFoundException;
import com.axis.model.Card;

public interface CardService {
	
	public String applyForNewCard(CreditCardApplyRequest creditCardReq)throws CreditCardAlreadyExistsException;
	public String changeDailyLimit(Integer id, double dailyLimit)throws CreditCardNotFoundException;
	public String activate(Integer id)throws CreditCardNotFoundException;
    public String deactivate(Integer id)throws CreditCardNotFoundException;
    public String changePin(Integer id, Integer newPin, Integer oldPin)throws CreditCardNotFoundException;

    public Card findCardById(Integer id)throws CreditCardNotFoundException;
	public Card findCardByAccountId(String accountId)throws CreditCardNotFoundException;
}
