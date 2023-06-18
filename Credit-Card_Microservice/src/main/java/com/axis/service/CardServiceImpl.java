package com.axis.service;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.axis.clients.TransactionClient;
import com.axis.dto.CreditCardApplyRequest;
import com.axis.dto.DeductCreditCardChargesRequest;
import com.axis.exception.CreditCardAlreadyExistsException;
import com.axis.exception.CreditCardNotFoundException;
import com.axis.model.Card;
import com.axis.model.CardType;
import com.axis.repository.CardRepository;
import com.axis.util.CreditCardNumberGenerator;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService{

    @Autowired
    private CardRepository cardRepository;
    
    @Autowired
    private CreditCardNumberGenerator creditCardNumberGenerator;

	@Autowired
	private TransactionClient transactionClient;
	
	
    
	@Override
	public String applyForNewCard(CreditCardApplyRequest creditCardReq)throws CreditCardAlreadyExistsException {
		
		
		Card alreadyExist = cardRepository.findCardByAccountId(creditCardReq.getAccountId());
		
		if(alreadyExist!=null) {
			throw new CreditCardAlreadyExistsException("Card Already exists with given account");
		}
		
		
		Card card = new Card();
		
		card.setAccountId(creditCardReq.getAccountId());
		
		card.setHolderName(creditCardReq.getHolderName());
		
		if(card.getCardType()==null) {
			card.setCardType(CardType.DISCOVERYCARD);			
		}
		
		int cardBin = card.getCardType().getNumVal();
		String cardBinString = Integer.toString(cardBin);
		
		card.setCardNumber(creditCardNumberGenerator.generate(cardBinString, 16));
		
		card.setCsv(creditCardNumberGenerator.cvvGenerator());
		
		Calendar cal = Calendar.getInstance();
		Date today = cal.getTime();
		cal.add(Calendar.YEAR, 5); // to get previous year add -1
		Date expirationDate = cal.getTime();
		
		card.setAllotmentDate(today);
		card.setExpiredDate(expirationDate);
		card.setStatus(true);
		card.setDailyLimit(100000.0);
		
		/* generate first time pin and send it by email */
		Random random = new Random();
		int firstPin = random.nextInt(9000) + 1000;
		
		card.setPin(firstPin);
		
		int issuenceCharge = 300;
		transactionClient.creditcardCharge(new DeductCreditCardChargesRequest(card.getAccountId(), issuenceCharge));			
	
		
		cardRepository.save(card);
		
		return "Applied for new card" ;
	}




	@Override
	public String changeDailyLimit(Integer id, double dailyLimit)  throws CreditCardNotFoundException{
		Optional<Card> cards = cardRepository.findById(id);
		if(cards.isEmpty()) {
			throw new CreditCardNotFoundException("Card Not found with id"+id);
		}
		if(dailyLimit>500000 && dailyLimit<1000) {
			return "Invalid DailyLimit";
		}
		
		Card card = cards.get();
		card.setDailyLimit(dailyLimit);
		cardRepository.save(card);
		return "Daily limit changed successfully";
	}

	@Override
	public String activate(Integer id)  throws CreditCardNotFoundException {
		Optional<Card> cards = cardRepository.findById(id);
		if(cards.isEmpty()) {
			throw new CreditCardNotFoundException("Card Not found with id"+id);
		}
		
		Card card = cards.get();
		card.setStatus(true);
		cardRepository.save(card);
		return "Card Activated Successfully";


	}

	@Override
	public String deactivate(Integer id)  throws CreditCardNotFoundException{
		Optional<Card> cards = cardRepository.findById(id);
		if(cards.isEmpty()) {
			throw new CreditCardNotFoundException("Card Not found with id"+id);
		}
		
		Card card = cards.get();
		card.setStatus(false);
		cardRepository.save(card);
		return "Card Deactivated Successfully";


	}
	
	@Override
	public Card findCardById(Integer id) throws CreditCardNotFoundException{
		Optional<Card> card = cardRepository.findById(id);
		if(card.isEmpty()) {
			throw new CreditCardNotFoundException("Card Not found with id"+id);
		}
		return card.get();
	}

	@Override
	public Card findCardByAccountId(String accountId)  throws CreditCardNotFoundException{
        return cardRepository.findCardByAccountId(accountId);
	}

    public String changePin(Integer id, Integer newPin, Integer oldPin)  throws CreditCardNotFoundException{
		Optional<Card> cards = cardRepository.findById(id);
		if(cards.isEmpty()) {
			throw new CreditCardNotFoundException("Card Not found with id"+id);
		}
		Card card = cards.get();
		
		
		if(card.getPin()!=oldPin) {
			return "Pin is Incorrect, Plz try again";
		}
		
		card.setPin(newPin);
		cardRepository.save(card);
		return "Pin changed successfully";
    }
    
    
    


}
