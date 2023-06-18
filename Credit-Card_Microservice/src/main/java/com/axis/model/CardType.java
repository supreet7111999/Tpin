package com.axis.model;

public enum CardType {
	AMERICANEXPRESSCARD(3),
	VISACARD(4),
	MASTERCARD(5),
	DISCOVERYCARD(6);
	
	
	private int numVal;
	
	CardType(int numVal){
		this.numVal=numVal;
	}

	public int getNumVal() {
	        return numVal;
    }

}


//The reason for starting with a 3,4,5, or 6 is that different card types start with different digit. The 3 is American Express, 4 is Visa, 5 is Master Card, 6 is a Discover Card.