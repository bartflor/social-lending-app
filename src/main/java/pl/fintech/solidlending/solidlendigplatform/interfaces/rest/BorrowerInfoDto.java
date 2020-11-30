package pl.fintech.solidlending.solidlendigplatform.interfaces.rest;

import lombok.Builder;
import lombok.Value;
import pl.fintech.solidlending.solidlendigplatform.domain.common.user.Borrower;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rating;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Value
public class BorrowerInfoDto {
	String name;
	String surname;
	String email;
	Double totalRating;
	List<OpinionDto> opinions;
	
	public static BorrowerInfoDto from(Borrower borrower){
		Rating rating = borrower.getRating();
		return BorrowerInfoDto.builder()
				.name(borrower.getUserDetails().getName())
				.surname(borrower.getUserDetails().getSurname())
				.email(borrower.getUserDetails().getEmail())
				.totalRating(rating.getTotalRating())
				.opinions(rating.getOpinions().stream()
						.map(OpinionDto::from)
						.collect(Collectors.toList()))
				.build();
				
	}
}

