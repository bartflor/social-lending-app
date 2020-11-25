package pl.fintech.solidlending.solidlendigplatform.domain.common.values;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ToString
@EqualsAndHashCode
@AllArgsConstructor
@Getter
public class Rating {
	Integer totalRating;
	List<Opinion> opinions;

	public Rating(){
		this.totalRating = 1;
		this.opinions = new ArrayList<>();
	}
	
	public void saveOpinion(Opinion newOpinion) {
		Optional<Opinion> oldOpinion = opinions.stream()
				.filter(opinion -> opinion.getInvestmentId().equals(newOpinion.getInvestmentId()))
				.findAny();
    	oldOpinion.ifPresentOrElse(opinion -> {opinion.setOpinionRating(newOpinion.getOpinionRating());
    									   opinion.setOpinionText(newOpinion.getOpinionText());},
			() -> opinions.add(newOpinion));
	}
	
}
