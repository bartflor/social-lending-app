package pl.fintech.solidlending.solidlendigplatform.interfaces.rest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Opinion;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OpinionDto {
	private Double opinionRating;
	private String opinionText;
	private String author;
	private Long investmentId;
	
	public Opinion toDomain() {
		return Opinion.builder()
				.author(getAuthor())
				.opinionRating(getOpinionRating())
				.opinionText(getOpinionText())
				.build();
	}
	
	public static OpinionDto from(Opinion opinion){
		return OpinionDto.builder()
				.opinionRating(opinion.getOpinionRating())
				.opinionText(opinion.getOpinionText())
				.author(opinion.getAuthor())
				.investmentId(opinion.getInvestmentId())
				.build();
	}
}
