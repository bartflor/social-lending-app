package pl.fintech.solidlending.solidlendigplatform.domain.common.values;

import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Builder
@Data
public class Opinion {
	Integer opinionRating;
	String opinionText;
	String author;
	Long investmentId;
	
	static Opinion makeOpinion(String userName, String opinion, Integer rating){
		if(rating>5){
			rating = 5;
		}
    	if (rating < 0) {
      		rating = 0;
		}
		return Opinion.builder()
				.author(userName)
				.opinionText(opinion)
				.opinionRating(rating)
				.build();
	}
}
