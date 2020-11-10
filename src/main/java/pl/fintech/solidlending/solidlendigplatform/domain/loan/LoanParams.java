package pl.fintech.solidlending.solidlendigplatform.domain.loan;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Value;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money;

import java.time.LocalDate;
import java.time.Period;
import java.util.Set;
@Value
@Builder
@EqualsAndHashCode
public class LoanParams {
  String borrowerUserName;
  Money loanAmount;
  Period loanDuration;
  LocalDate loanStartDate;
  Set<Investment> investments;

}
