package pl.fintech.solidlending.solidlendigplatform.domain.common.user;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Opinion;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rating;
@SuperBuilder
@Getter
@EqualsAndHashCode
public class Borrower extends User {
  private Rating rating;
  
  public void giveOpinion(Opinion opinion) {
    rating.saveOpinion(opinion);
  }
}
