package com.bankslips.rest.businessrules;

public enum InterestRateEnum {
	INITIAL_RATE(1, 0.005D),
	FINAL_RATE(11, 0.01D);

	private int startsIn;
	private double interestRate;

	InterestRateEnum(int startsIn, double interestRate) {
		this.startsIn = startsIn;
		this.interestRate = interestRate;
	}

	public int getStartsIn() {
		return startsIn;
	}

	public double getInterestRate() {
		return interestRate;
	}	
}