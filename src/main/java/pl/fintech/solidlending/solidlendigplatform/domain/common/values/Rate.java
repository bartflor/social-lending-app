package pl.fintech.solidlending.solidlendigplatform.domain.common.values;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.exception.ValueNotAllowedException;

import java.math.BigDecimal;

@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class Rate {
	private static final String VALUE_NOT_ALLOWED = "Value not allowed. Percent value must be in range (0; 100). Provided: %s";
	private BigDecimal percentValue;// 0 <= percentValue <= 100
	
	public static Rate fromPercentDouble(Double doubleValue){
		if(doubleValue > 100 || doubleValue < 0){
			throw new ValueNotAllowedException(String.format(VALUE_NOT_ALLOWED, doubleValue));
		}
		return new Rate(BigDecimal.valueOf(doubleValue/100));
	}
	
	public BigDecimal getDecimalValue(){
		return percentValue.divide(BigDecimal.valueOf(100));
	}
	
	public BigDecimal getPercentValue(){
		return percentValue;
	}
}
