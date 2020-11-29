package pl.fintech.solidlending.solidlendigplatform.domain.common.values;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.exception.ValueNotAllowedException;

import java.math.BigDecimal;

@ToString
@EqualsAndHashCode
public class Rate {
	private static final String PERCENT_VALUE_NOT_ALLOWED = "Value not allowed. Percent value must be in range (0; 100). Provided: %s";

	private BigDecimal percentValue;// 0 <= percentValue <= 100
	
	private Rate(BigDecimal percentValue){
		this.percentValue = percentValue;
	}
	public static Rate fromPercentValue(Double percentValue){
		if(percentValue > 100 || percentValue < 0){
			throw new ValueNotAllowedException(String.format(PERCENT_VALUE_NOT_ALLOWED, percentValue));
		}
		return new Rate(BigDecimal.valueOf(percentValue));
	}
	
	public BigDecimal getDecimalValue(){
		return percentValue.divide(BigDecimal.valueOf(100));
	}
	
	public BigDecimal getPercentValue(){
		return percentValue;
	}
}
