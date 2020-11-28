package pl.fintech.solidlending.solidlendigplatform.domain.loan;

import lombok.Builder;
import lombok.Data;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rate;

import java.time.Period;

@Data
@Builder
public class Investment {
  Long investmentId;
  Long loanId;
  String lenderName;
  String borrowerName;
  Money returnAmount;
  Money loanAmount;
  Rate rate;
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
