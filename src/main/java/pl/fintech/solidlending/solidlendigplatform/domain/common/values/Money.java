package pl.fintech.solidlending.solidlendigplatform.domain.common.values;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
@AllArgsConstructor
@Data
public class Money {
	public static final Money ZERO = new Money(0);
	private BigDecimal value;
	
	public Money(double loanAmount) {
		value = new BigDecimal(loanAmount);
		//TODO implement
	}
	
	public boolean isMoreOrEqual(Money amount) {
		return value.compareTo(amount.getValue()) >= 0;
	}
	
	public Money divide(long num){
		return new Money(value.divide(BigDecimal.valueOf(num)));
	}
	public static Money sum(Money money1, Money money2) {
		return new Money(money1.getValue().add(money2.getValue()));
	}
}
