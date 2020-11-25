package pl.fintech.solidlending.solidlendigplatform.interfaces.rest;

import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.Borrower;
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.UserDetails;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rate;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rating;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Value
public class BorrowerInfoDto {
	String email;
	Integer totalRating;
	List<OpinionDto> opinions;
	
	public static BorrowerInfoDto from(Borrower borrower){
		Rating rating = borrower.getRating();
		return BorrowerInfoDto.builder()
				.email(borrower.getUserDetails().getEmail())
				.totalRating(rating.getTotalRating())
				.opinions(rating.getOpinions().stream()
						.map(OpinionDto::from)
						.collect(Collectors.toList()))
				.build();
				
	}
}

