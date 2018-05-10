package com.bankslips.rest.businessrules;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class FineCalc {

	/**
	 * This method perform the fine calculation, roudingMode = HALF_EVEN
	 * @param bankSlipvalue
	 * @param dueDate
	 * @return Long
	 */
	public static Long calculateFine(long bankSlipvalue, LocalDate dueDate) {

		BigDecimal fine = new BigDecimal(0);

		//getting the difference between dueDate and actual date
		LocalDate localDate = LocalDate.now();
		long overdueDays = ChronoUnit.DAYS.between(dueDate, localDate);

		//verify if is overdue date, if yes initialize the fine variable with the initial bankslip value
		/*if (overdueDays > 0) {
			fine = new BigDecimal(bankSlipvalue).setScale(2, RoundingMode.HALF_EVEN);
		}*/
	
		//Performing the interest calculation
		for (int i = 0; i <= overdueDays; i++) {
			if(i >= InterestRateEnum.INITIAL_RATE.getStartsIn() && i < InterestRateEnum.FINAL_RATE.getStartsIn() ) {
				fine = fine.add(new BigDecimal(InterestRateEnum.INITIAL_RATE.getInterestRate() * bankSlipvalue));
			}else if (i >=  InterestRateEnum.FINAL_RATE.getStartsIn()) {
				fine = fine.add(new BigDecimal(InterestRateEnum.FINAL_RATE.getInterestRate() * bankSlipvalue));
			}
		}
		fine.setScale(2, RoundingMode.HALF_EVEN);
		return fine.longValue();
	}
}
