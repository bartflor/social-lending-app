package pl.fintech.solidlending.solidlendigplatform.domain.loan;

import lombok.Builder;
import lombok.Data;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rate;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Risk;

import java.time.Period;

@Data
@Builder
public class Investment {
  Long investmentId;
  Long loanId;
  String lenderName;
  String borrowerName;
  Money value;
  Money loanAmount;
  Rate rate;
  Risk risk;
  Period duration;
  @Builder.Default Status status = Status.UNCONFIRMED;
  RepaymentSchedule schedule;
  
  public void makeCompleted() {
        setStatus(Status.COMPLETED);
  }
  
  public enum Status {
    UNCONFIRMED,
    ACTIVE,
    COMPLETED,
    LATE
  }
}
