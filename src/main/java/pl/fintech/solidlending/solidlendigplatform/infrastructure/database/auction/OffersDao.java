package pl.fintech.solidlending.solidlendigplatform.infrastructure.database.auction;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.Offer;
import pl.fintech.solidlending.solidlendigplatform.domain.auction.OfferRepository;

import javax.swing.text.html.parser.Entity;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class OffersDao implements OfferRepository {
	
	private JpaOffersRepository jpaOffersRepository;
	
	@Override
	public Long save(Offer offer) {
		OfferEntity savedOffer = jpaOffersRepository.save(OfferEntity.createFromOffer(offer));
		return savedOffer.getId();
	}
	
	@Override
	public List<Offer> findAllByUserName(String userName) {
		return jpaOffersRepository.findAllByLenderName(userName).stream()
				.map(OfferEntity::toDomain)
				.collect(Collectors.toList());
	}
	
	@Override
	public List<Offer> findAll() {
		return jpaOffersRepository.findAll().stream()
				.map(OfferEntity::toDomain)
				.collect(Collectors.toList());
	}
	
	@Override
	public Optional<Offer> findById(Long offerId) {
		return jpaOffersRepository.findById(offerId).map(OfferEntity::toDomain);
	}
	
	@Override
	public void update(Long id, Offer offer) {
		offer.setId(id);
		jpaOffersRepository.save(OfferEntity.createFromOffer(offer));
	}
}
