package pl.fintech.solidlending.solidlendigplatform.interfaces.rest;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import pl.fintech.solidlending.solidlendigplatform.domain.loan.Repayment;

import java.math.BigDecimal;
import java.time.Instant;
@Data
@Builder
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public class RepaymentDto {
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "Europe/Warsaw")
	Instant date;
	BigDecimal value;
	Repayment.Status status;
	
	public static RepaymentDto from(Repayment repayment) {
		return RepaymentDto.builder()
				.date(repayment.getDate())
				.value(repayment.getValue().getValue())
				.status(repayment.getStatus())
				.build();
	}
}
