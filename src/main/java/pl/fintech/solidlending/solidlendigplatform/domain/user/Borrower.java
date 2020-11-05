package pl.fintech.solidlending.solidlendigplatform.domain.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rating;
@SuperBuilder
@Getter
public class Borrower extends User {
  private Rating rating;
}
