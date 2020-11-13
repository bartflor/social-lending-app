package pl.fintech.solidlending.solidlendigplatform.domain.loan;

import lombok.Builder;
import lombok.Data;
import lombok.Setter;
import lombok.Value;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rate;

import java.time.Period;

@Data
@Builder
public class Investment {
  Long investmentId;
  Long loanId;
  String lenderName;
  Money value;
  Money startAmount;
  Rate rate;
  Period duration;
  @Builder.Default Status status = Status.UNCONFIRMED;
  RepaymentSchedule schedule;

  public enum Status {
    UNCONFIRMED,
    ACTIVE,
    COMPLETED,
    OVERDUE
  }
}
