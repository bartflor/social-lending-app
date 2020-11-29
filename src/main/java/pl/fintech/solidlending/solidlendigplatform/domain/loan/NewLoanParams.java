package pl.fintech.solidlending.solidlendigplatform.domain.loan;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money;

import java.time.Instant;
import java.time.Period;
import java.util.List;

@Value
@Builder
@EqualsAndHashCode
class NewLoanParams {
  String borrowerUserName;
  Money loanAmount;
  Period loanDuration;
  Instant loanStartDate;
  List<NewInvestmentParams> investmentsParams;

}
