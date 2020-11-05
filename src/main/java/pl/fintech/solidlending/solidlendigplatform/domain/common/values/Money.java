package pl.fintech.solidlending.solidlendigplatform.domain.common.values;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
@AllArgsConstructor
@Data
public class Money {
	private BigDecimal value;
	
	public Money(double loanAmount) {
		value = new BigDecimal(loanAmount);
		//TODO implement
	}
	
	public boolean isMoreThan(Money amount) {
		return value.compareTo(amount.getValue()) > 0;
	}
}
