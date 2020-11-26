package pl.fintech.solidlending.solidlendigplatform.infrastructure.database.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Opinion;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OpinionEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	Double opinionRating;
	String opinionText;
	String author;
	Long investmentId;
	
	public Opinion toDomain(){
		return Opinion.builder()
				.author(getAuthor())
				.opinionRating(getOpinionRating())
				.opinionText(getOpinionText())
				.investmentId(getInvestmentId())
				.build();
				
	}
	
	public static OpinionEntity from(Opinion opinion){
		return OpinionEntity.builder()
				.author(opinion.getAuthor())
				.opinionRating(opinion.getOpinionRating())
				.opinionText(opinion.getOpinionText())
				.investmentId(opinion.getInvestmentId())
				.build();
	}
}

