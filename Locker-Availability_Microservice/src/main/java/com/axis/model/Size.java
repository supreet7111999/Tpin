package com.axis.model;

public enum Size {
	SMALL (1700),
	MEDIUM (2500),
	LARGE (5500),
	EXTRALARGE (11000);

	private int numVal;
	
	Size(int numVal){
		this.numVal=numVal;
	}

	 public int getNumVal() {
	        return numVal;
	    }

}
